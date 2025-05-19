package cc.sukazyo.nukos.carpet.pets.protect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public interface PetProtectionChecker {
	
	boolean shouldProtect (PlayerEntity damageSource, LivingEntity target);
	
}
