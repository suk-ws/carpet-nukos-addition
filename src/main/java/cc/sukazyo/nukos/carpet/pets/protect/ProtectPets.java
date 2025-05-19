package cc.sukazyo.nukos.carpet.pets.protect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

//import static cc.sukazyo.nukos.carpet.ModCarpetNukos.LOGGER;

public class ProtectPets implements PetProtectionChecker {
	
	public static class Builder implements PetProtectionBuilder {
		public static final Builder INSTANCE = new Builder();
		@Override
		public Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException {
			if (config.startsWith("pets")) {
				return switch (config) {
					case "pets" -> throw new IllegalConfigException("all pets supergroup is not implemented yet.");
					case "pets/owned" -> Optional.of(new ProtectPets());
					case "pets/team" -> throw new IllegalConfigException("pets/team supergroup is not implemented yet.");
					default -> throw new IllegalConfigException("%s is not a valid config for pets superset.".formatted(config));
				};
			} else {
				return Optional.empty();
			}
		}
	}
	
	@Override
	public boolean shouldProtect (PlayerEntity damageSource, LivingEntity target) {
		
		if (target instanceof Tameable pet) {
//			LOGGER.debug("is a pet");
			LivingEntity owner = pet.getOwner();
			
			if (owner != null) {
//				LOGGER.debug("pet owner is %s.".formatted(owner.getEntityName()));
				if (owner.getUuid().equals(damageSource.getUuid())) {
//					LOGGER.debug("damage source is owner, skips the attack");
					return true;
				}
			} else {
//				LOGGER.debug("pet has no owner currently");
			}
			
		}
		
		return false;
		
	}
	
}
