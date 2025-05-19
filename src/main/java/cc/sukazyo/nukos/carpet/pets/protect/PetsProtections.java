package cc.sukazyo.nukos.carpet.pets.protect;

import carpet.api.settings.CarpetRule;
import carpet.api.settings.Validator;
import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static cc.sukazyo.nukos.carpet.ModCarpetNukos.LOGGER;

public class PetsProtections {
	
	public static List<PetProtectionBuilder> petProtectionBuilders = new ArrayList<>();
	static {
		petProtectionBuilders.add(ProtectBaby.Builder.INSTANCE);
		petProtectionBuilders.add(ProtectPets.Builder.INSTANCE);
		petProtectionBuilders.add(ProtectVillages.Builder.INSTANCE);
		petProtectionBuilders.add(ProtectEntityOf.Builder.INSTANCE);
	}
	
	public static void onLivingEntityHurts (LivingEntity entity, DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
		
		if (!(source.getAttacker() instanceof PlayerEntity player)) {
			return;
		}
//		LOGGER.debug("damage is from player " + player.getEntityName());
//		LOGGER.debug("Hurting entity ❤️%f: [%s] %s".formatted(
//				damage,
//				Registries.ENTITY_TYPE.getId(entity.getType()).toString(),
//				entity.getEntityName()
//		));
		
		if (player.isSneaking()) {
//			LOGGER.debug("player is sneaking, bypass protects");
			return;
		}
		
		for (PetProtectionChecker checker : CarpetNukosSettings.getAnimalDamageImmuneConfigs()) {
			
			if (checker.shouldProtect(player, entity)) {
				
				entity.setAttacker(null);
				player.setAttacker(null);
				if (player instanceof ServerPlayerEntity serverPlayer) {
					serverPlayer.sendMessage(
							Text.translatable(
									"carpet_nukos_add.pets.damage_protected",
									entity.getDisplayName()
							), true);
				}
				cir.setReturnValue(false);
				return;
				
			}
			
		}
		
	}
	
	public static PetProtectionChecker[] protectionsFromConfig (String config) throws PetProtectionBuilder.IllegalConfigException {
		
		if (config.isEmpty()) return new PetProtectionChecker[0];
		
		final List<PetProtectionChecker> checkers = new ArrayList<>();
		final String[] configs = config.split(",");
		for (final String configItem : configs) {
			boolean isSuccess = false;
			for (final PetProtectionBuilder builder : petProtectionBuilders) {
				Optional<PetProtectionChecker> result = builder.fromConfig(configItem);
				if (result.isPresent()) {
					checkers.add(result.get());
					isSuccess = true;
				}
			}
			if (!isSuccess) {
				throw new PetProtectionBuilder.IllegalConfigException("%s isn't either a supported superset name nor an entity id".formatted(configItem));
			}
		}
		return checkers.toArray(new PetProtectionChecker[0]);
		
	}
	
	public static class ProtectionConfigValidator extends Validator<String> {
		@Override
		public String validate (
				@Nullable ServerCommandSource source, CarpetRule<String> changingRule, String newValue,
				String userInput
		) {
			try {
				protectionsFromConfig(userInput);
				return userInput;
			} catch (PetProtectionBuilder.IllegalConfigException e) {
				LOGGER.error(e.getMessage());
				return null;
			}
		}
	}
	
}
