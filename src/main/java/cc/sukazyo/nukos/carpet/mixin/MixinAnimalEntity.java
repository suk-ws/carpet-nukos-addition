package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.pets.breeding.BreedingFilter;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnimalEntity.class)
public abstract class MixinAnimalEntity extends PassiveEntity {
	protected MixinAnimalEntity (EntityType<? extends PassiveEntity> entityType, World world) {
		super(entityType, world);
	}
	
	@Inject(method = "interactMob", at = @At(
			value = "INVOKE_ASSIGN",
			target = "Lnet/minecraft/entity/passive/AnimalEntity;canEat()Z",
			ordinal = 0
	), cancellable = true)
	public void onInteractBreedingLove (PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		AnimalEntity self = (AnimalEntity) (Object) this;
		
		BreedingFilter.onInteractBreedingLove(self, player, hand, cir);
		
	}
	
}
