package cc.sukazyo.nukos.carpet.pets.protect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.Optional;

public class ProtectVillages implements PetProtectionChecker {
	
	public static class Builder implements PetProtectionBuilder {
		public static final Builder INSTANCE = new Builder();
		@Override
		public Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException {
			if (config.startsWith("villages")) {
				if (config.equals("villages")) {
					return Optional.of(new ProtectVillages());
				} else {
					throw new IllegalConfigException("villages supergroup does not support subgroups yet.");
				}
			} else {
				return Optional.empty();
			}
		}
	}
	
	@Override
	public boolean shouldProtect (PlayerEntity damageSource, LivingEntity target) {
		
		return target instanceof MerchantEntity;
		
	}
	
}
