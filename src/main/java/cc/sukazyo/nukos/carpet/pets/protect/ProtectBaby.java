package cc.sukazyo.nukos.carpet.pets.protect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

//import static cc.sukazyo.nukos.carpet.ModCarpetNukos.LOGGER;

public class ProtectBaby implements PetProtectionChecker {
	
	public static class Builder implements PetProtectionBuilder {
		public static final Builder INSTANCE = new Builder();
		@Override
		public Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException {
			if (config.startsWith("baby")) {
				return switch (config) {
					case "baby" -> Optional.of(new ProtectBaby(false));
					case "baby/animal" -> Optional.of(new ProtectBaby(true));
					default -> throw new IllegalConfigException("%s is not a valid config for baby superset.".formatted(config));
				};
			} else {
				return Optional.empty();
			}
		}
	}
	
	private final boolean animalOnly;
	
	public ProtectBaby(final boolean animalOnly) {
		this.animalOnly = animalOnly;
	}
	
	@Override
	public boolean shouldProtect (PlayerEntity damageSource, LivingEntity target) {
		if (target.isBaby()) {
			
			if (animalOnly) {
				if (target instanceof AnimalEntity) {
					if (target instanceof Monster) {
//						LOGGER.debug("this baby is a monster, should not protect");
						return false;
					}
//					LOGGER.debug("damage is to baby animals, skips the attack");
					return true;
				} else {
//					LOGGER.debug("this baby is not an animal, should not protect");
					return false;
				}
			}
			
//			LOGGER.debug("damage is to babies, skips the attack");
			return true;
			
		}
		return false;
	}
	
}
