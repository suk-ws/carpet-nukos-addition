package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import cc.sukazyo.nukos.carpet.anvils.AnvilItemCostRollupAlgorithm;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class MixinAnvilScreenHandler
		extends ForgingScreenHandler {
	
	@Shadow
	@Final private Property levelCost;
	@Shadow
	private int repairItemUsage;
	@Shadow
	private String newItemName;
	
	public MixinAnvilScreenHandler (
			@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
			ScreenHandlerContext context
	) {
		super(type, syncId, playerInventory, context);
	}
	
//	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
//	public void inject_updateResult(CallbackInfo ci) {
//		if (false) {
//			ci.cancel();
//		}
//	}
	
	@Inject(method = "getNextCost", at = @At(value = "HEAD"), cancellable = true)
	private static void getNextCost_inject (int cost, CallbackInfoReturnable<Integer> cir) {
		final AnvilItemCostRollupAlgorithm algorithm = AnvilItemCostRollupAlgorithm.Validator.getFromName(
				CarpetNukosSettings.anvilItemCostRollupAlgorithm
		);
		if (algorithm instanceof AnvilItemCostRollupAlgorithm.Customized) {
			cir.setReturnValue(
					((AnvilItemCostRollupAlgorithm.Customized)algorithm).getNextCost(cost)
			);
		} else if (algorithm instanceof AnvilItemCostRollupAlgorithm.Vanilla) {
			// Nothing to do
		} else {
			ModCarpetNukos.LOGGER.warn(
					"Unknown next anvil item cost algorithm:{} : {}",
					algorithm.getClass().getName(),
					algorithm
			);
		}
	}
	
}
