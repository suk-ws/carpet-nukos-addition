package cc.sukazyo.nukos.carpet;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;
import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithm;
import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithms;
import cc.sukazyo.nukos.carpet.anvils.AnvilItemCostRollupAlgorithm;
import cc.sukazyo.nukos.carpet.CarpetAdditionNukos.NukosCategoryKeys;
import cc.sukazyo.nukos.carpet.pets.protect.*;
import cc.sukazyo.nukos.carpet.text.anvil.MyAnvilTextHelpers;

import static cc.sukazyo.nukos.carpet.ModCarpetNukos.LOGGER;

public class CarpetNukosSettings {
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"vanilla", "linear:1", "no-change", "fixed:0"},
			strict = false,
			validators = AnvilItemCostRollupAlgorithm.Validator.class
	)
	public static String anvilItemCostRollupAlgorithm = "vanilla";
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"vanilla", "vanilla-reforged"},
			strict = false,
			validators = AnvilAlgorithms.Validator.class
	)
	public static String anvilAlgorithm = "vanilla";
	
	public static AnvilAlgorithm getCurrentAnvilAlgorithm () {
		return AnvilAlgorithms.getFromName(CarpetNukosSettings.anvilAlgorithm);
	}
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL})
	public static Boolean anvilUseRenameCost = true;
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL})
	public static Boolean anvilUseItemCost = true;
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"40", Integer.MAX_VALUE + ""},
			strict = false
	)
	public static int anvilTooExpensiveLimit = 40;
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"vanilla", "escaped", "escape-and", "mini-message"},
			strict = false,
			validators = MyAnvilTextHelpers.Validator.class
	)
	public static String anvilCustomNameSerializer = "vanilla";
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL})
	public static boolean anvilCanRepairUseIronBlock = false;
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.OPTIMIZATION, RuleCategory.EXPERIMENTAL, NukosCategoryKeys.TICK})
	public static Boolean tickFreezeWhenNoPlayers = false;
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.OPTIMIZATION, RuleCategory.EXPERIMENTAL, NukosCategoryKeys.TICK})
	public static Boolean tickFreezeWhenNoPlayersUseDeepFreeze = false;
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.OPTIMIZATION, RuleCategory.EXPERIMENTAL, NukosCategoryKeys.TICK},
			options = {"", "MyBot", "*bot,*Bot"},
			strict = false
	)
	public static String ignoringPlayersOnAutoTickFreeze = "";
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.TICK},
			options = {"", "MyBot", "*bot,*Bot"},
			strict = false
	)
	public static String ignoringPlayersOnSleeping = "";
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, NukosCategoryKeys.ENTITY, NukosCategoryKeys.PETS},
			options = {"none", "pets/owned", "pets/team,baby/animal", "pets,baby/animal,villages"},
			strict = false,
			validators = PetsProtections.ProtectionConfigValidator.class
	)
	public static String animalDamageImmune = "";
	public static PetProtectionChecker[] getAnimalDamageImmuneConfigs () {
		try {
			return PetsProtections.protectionsFromConfig(animalDamageImmune);
		} catch (PetProtectionBuilder.IllegalConfigException ignored) {
			LOGGER.warn("failed to load animal friendly fire configs!");
			return new PetProtectionChecker[0];
		}
	}
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, NukosCategoryKeys.ENTITY, NukosCategoryKeys.PETS},
			options = {"none", "cat"}
	)
	public static String petsNoAccidentBreeding = "none";
	
}
