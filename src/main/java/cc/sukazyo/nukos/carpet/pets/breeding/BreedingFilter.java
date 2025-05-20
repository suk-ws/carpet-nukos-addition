package cc.sukazyo.nukos.carpet.pets.breeding;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class BreedingFilter {
	
	public static void onInteractBreedingLove (AnimalEntity animal, PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		
		if (CarpetNukosSettings.petsNoAccidentBreeding.equals("cat") && animal instanceof CatEntity) {
			if (!player.isSneaking()) {
				if (player instanceof ServerPlayerEntity serverPlayer) {
					serverPlayer.sendMessage(
							Text.translatable(
									"carpet_nukos_add.pets.full_health.message",
									animal.getDisplayName()
							), true);
				}
				cir.setReturnValue(ActionResult.PASS);
			}
		}
		
	}
	
}
