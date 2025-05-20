package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.pets.protect.PetsProtections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	public MixinLivingEntity (EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Inject(method = "damage", at = @At("HEAD"), cancellable = true)
	public void onEntityHurts (DamageSource damageSource, float amount, CallbackInfoReturnable<Boolean> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		
		PetsProtections.onLivingEntityHurts(self, damageSource, amount, cir);
		
	}
	
}
