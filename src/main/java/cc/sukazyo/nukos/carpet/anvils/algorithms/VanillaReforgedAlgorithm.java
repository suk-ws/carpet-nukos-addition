package cc.sukazyo.nukos.carpet.anvils.algorithms;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithm;
import cc.sukazyo.nukos.carpet.anvils.AnvilContext;
import cc.sukazyo.nukos.carpet.anvils.AnvilInputContext;
import cc.sukazyo.nukos.carpet.anvils.AnvilResult;
import cc.sukazyo.nukos.carpet.utils.TextDecomposer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.util.Util;

import java.util.Map;
import java.util.Optional;

public class VanillaReforgedAlgorithm implements AnvilAlgorithm {
	
	public static class AnvilActions {
		
		public boolean ingot_repaired = false;
		public boolean merged_damage = false;
		public boolean merged_magic = false;
		public boolean renamed = false;
		
		public boolean isOnlyRenamed () {
			return renamed && !merged_damage && !merged_magic && !ingot_repaired;
		}
		
		public boolean isActed () {
			return renamed || merged_damage || merged_magic || ingot_repaired;
		}
		
	}
	
	public static int getAnvilUseCostOfItems (ItemStack item1, ItemStack item2) {
		return item1.getRepairCost() + (
				item2.isEmpty() ? 0 : item2.getRepairCost()
		);
	}
	
	public static boolean isAvailableEnchantBook (ItemStack item) {
		return item.isOf(Items.ENCHANTED_BOOK) &&
				!EnchantedBookItem.getEnchantmentNbt(item).isEmpty();
	}
	
	public static int mergeMagic (int sourceLevel, int materialLevel) {
		if (sourceLevel == materialLevel)
			return sourceLevel + 1;
		return Math.max(sourceLevel, materialLevel);
	}
	
	public static int getMagicCost (Enchantment magic, int level, boolean isUsingBook) {
		int costBase = switch (magic.getRarity()) {
			case COMMON -> 1;
			case UNCOMMON -> 2;
			case RARE -> 4;
			case VERY_RARE ->  8;
		};
		if (isUsingBook) costBase = Math.max(1, costBase / 2);
		return costBase * level;
	}
	
	@Override
	public Optional<AnvilResult> updateResult (AnvilInputContext context) {
		
		// directly fail when the main input has nothing
		if (context.input1.isEmpty()) { return Optional.of(AnvilResult.empty()); }
		
		ItemStack itemOutput = context.input1.copy();
		AnvilActions actions = new AnvilActions();
		int expCostBase = 0;
		Map<Enchantment, Integer> itemOutputMagics = EnchantmentHelper.get(itemOutput);
		int input2UsedCount = 0;
		
		// do merge/repair with two items
		if (!context.input2.isEmpty()) {
			final boolean isEnchantUsingBook = isAvailableEnchantBook(context.input2);
			// do repair the damageable item with its ingredient
			if (itemOutput.isDamageable() && itemOutput.getItem().canRepair(context.input1, context.input2)) {
				// get the damage will be repair, 25% max (for the first time)
				int _damageWillRepair = Math.min(itemOutput.getDamage(), itemOutput.getMaxDamage() / 4);
				// does not need repair, then fails
				if (_damageWillRepair <= 0)
					return Optional.of(AnvilResult.empty());
				int _ingotUsed = 0;
				for (; _damageWillRepair > 0 && _ingotUsed < context.input2.getCount(); ++_ingotUsed) {
					// do repair the damage
					itemOutput.setDamage(itemOutput.getDamage() - _damageWillRepair);
					++expCostBase;
					// check if it needs to continue repair and set the new repairable damage.
					_damageWillRepair = Math.min(itemOutput.getDamage(), itemOutput.getMaxDamage() / 4);
				}
				input2UsedCount = _ingotUsed;
				actions.ingot_repaired = true;
			// do merge two items
			} else {
				// if two items cannot merge, then fails
				if (!(isEnchantUsingBook || itemOutput.isOf(context.input2.getItem()) && itemOutput.isDamageable())) {
					return Optional.of(AnvilResult.empty());
				}
				// merge to item's damage
				if (itemOutput.isDamageable() && !isEnchantUsingBook) {
					final int input1DamageRemain = context.input1.getMaxDamage() - context.input1.getDamage();
					final int input2DamageRemain = context.input2.getMaxDamage() - context.input2.getDamage();
					final int itemDamageProvided = input2DamageRemain + itemOutput.getMaxDamage() * 12 / 100;
					final int newDamageRemain = input1DamageRemain + itemDamageProvided;
					final int newDamage = Math.max(itemOutput.getMaxDamage() - newDamageRemain, 0);
					if (newDamage < itemOutput.getDamage()) {
						itemOutput.setDamage(newDamage);
						expCostBase += 2;
						actions.merged_damage = true;
					}
				}
				/* assume it is <EnchantmentType, EnchantmentLevel> */
				final Map<Enchantment, Integer> item2Magics = EnchantmentHelper.get(context.input2);
				boolean isAnyMagicAvailable = false;
				boolean isAnyMagicUnavailable = false;
				for (Enchantment magic : item2Magics.keySet()) {
					if (magic == null) continue;
					final int magicLevelSource = itemOutputMagics.getOrDefault(magic, 0);
					final int magicLevelMaterial = item2Magics.get(magic);
					int magicLevelNew = mergeMagic(magicLevelSource, magicLevelMaterial);
					// check is magic available on item
					boolean isMagicAcceptable = magic.isAcceptableItem(context.input1);
					if (context.player.getAbilities().creativeMode || context.input1.isOf(Items.ENCHANTED_BOOK)) {
						isMagicAcceptable = true;
					}
					// check magic conflict
					for (Enchantment magicInput : itemOutputMagics.keySet()) {
						if (magicInput == magic || magic.canCombine(magicInput)) continue;
						isMagicAcceptable = false;
						++expCostBase;
					}
					// if this magic available, then continue, or else this magic will be ignored
					if (!isMagicAcceptable) {
						isAnyMagicUnavailable = true;
						continue;
					}
					isAnyMagicAvailable = true;
					// check magic max level
					if (magicLevelNew > magic.getMaxLevel()) {
						magicLevelNew = magic.getMaxLevel();
					}
					itemOutputMagics.put(magic, magicLevelNew);
					// add cost based on magic rarity and level
					expCostBase += getMagicCost(magic, magicLevelNew, isEnchantUsingBook);
					// if adding magic to one item, then continue, else directly fails
					if (context.input1.getCount() <= 1) continue;
					expCostBase = Integer.MAX_VALUE;
				}
				// if every magic is unavailable, then fails
				if (isAnyMagicUnavailable && !isAnyMagicAvailable)
					return Optional.of(AnvilResult.empty());
				if (isAnyMagicAvailable)
					actions.merged_magic = true;
			}
		}
		
		// if reset custom name
		if (context.newItemName == null || Util.isBlank(context.newItemName)) {
			if (context.input1.hasCustomName()) {
				if (CarpetNukosSettings.anvilUseRenameCost)
					expCostBase += 1;
				itemOutput.removeCustomName();
				actions.renamed = true;
			}
		// if set new custom name
		} else if (!context.newItemName.equals(context.input1.getName().getString())) {
			if (CarpetNukosSettings.anvilUseRenameCost)
				expCostBase += 1;
			itemOutput.setCustomName(TextDecomposer.toFormattedComponent(context.newItemName));
			actions.renamed = true;
		}
		
		// set final cost is the base cost adds item cost
		int levelCost;
		if (CarpetNukosSettings.anvilUseItemCost) {
			levelCost = getAnvilUseCostOfItems(context.input1, context.input2) + expCostBase;
		} else {
			levelCost = expCostBase;
		}
		// if nothing does, then fails
		if (!actions.isActed()) {
			return Optional.of(AnvilResult.empty());
		}
		// if there's only rename then cost is 39 max
		if (actions.isOnlyRenamed() && levelCost >= 40) {
			levelCost = 39;
		}
		
		// if the final cost is above setting anvil-limit (defaults 40), then it is TOO EXPENSIVE
		if (
				CarpetNukosSettings.anvilTooExpensiveLimit >= 0
				&& levelCost >= CarpetNukosSettings.anvilTooExpensiveLimit
				&& !context.player.getAbilities().creativeMode
		) {
			levelCost = Integer.MAX_VALUE;
			itemOutput = ItemStack.EMPTY;
		}
		if (!itemOutput.isEmpty()) {
			
			// rolling up repair cost for the new item
			int nextItemUseCost = itemOutput.getRepairCost();
			if (!context.input2.isEmpty() && nextItemUseCost < context.input2.getRepairCost()) {
				nextItemUseCost = context.input2.getRepairCost();
			}
			if (!actions.isOnlyRenamed()) {
				nextItemUseCost = AnvilScreenHandler.getNextCost(nextItemUseCost);
			}
			itemOutput.setRepairCost(nextItemUseCost);
			
			// set the new magics
			EnchantmentHelper.set(itemOutputMagics, itemOutput);
			
		}
		
		return Optional.of(AnvilResult.empty()
				.setOutput(itemOutput)
				.setLevelCost(levelCost)
				.setIngotUsed(input2UsedCount));
		
	}
	
	@Override
	public Optional<Boolean> canTakeout (AnvilContext context) {
		if (context.output.isEmpty())
			return Optional.of(false);
		if (context.player.getAbilities().creativeMode)
			return Optional.of(true);
		if (context.player.experienceLevel >= context.levelCost)
			return Optional.of(true);
		return Optional.of(false);
	}
	
}
