package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import cc.sukazyo.nukos.carpet.anvils.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

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
	
	@Inject(method = "setNewItemName", at = @At("HEAD"), cancellable = true)
	public void inject_setNewItemName (String newItemName, CallbackInfoReturnable<Boolean> cir) {
		Optional<AnvilSetNewNameContext.ReturnedContext> result = CarpetNukosSettings.getCurrentAnvilAlgorithm().setNewName(
				new AnvilSetNewNameContext(
						newItemName,
						this.newItemName,
						this.getSlot(2),
						(newName -> this.newItemName = newName)
				)
		);
		result.ifPresent(ret -> {
			this.updateResult();
			cir.setReturnValue(ret.isSuccess());
		});
	}
	
	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	public void inject_canTakeOutput (PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		AnvilContext context = new AnvilContext(
				this.input.getStack(0), this.input.getStack(1),
				this.newItemName,
				this.player,
				this.output.getStack(0),
				this.levelCost.get(), this.repairItemUsage
		);
		Optional<Boolean> tryResult = CarpetNukosSettings.getCurrentAnvilAlgorithm().canTakeout(context);
		tryResult.ifPresent(cir::setReturnValue);
	}
	
	@Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
	public void inject_updateResult(CallbackInfo ci) {
		AnvilInputContext context = new AnvilInputContext(
				this.input.getStack(0), this.input.getStack(1),
				this.newItemName,
				this.player
		);
		Optional<AnvilResult> tryResult = CarpetNukosSettings.getCurrentAnvilAlgorithm().updateResult(context);
		if (tryResult.isPresent()) {
			AnvilResult result = tryResult.get();
			this.output.setStack(0, result.output);
			this.levelCost.set(result.levelCost);
			this.repairItemUsage = result.ingotUsed;
			ci.cancel();
		}
	}
	
	@Inject(method = "getNextCost", at = @At(value = "HEAD"), cancellable = true)
	private static void getNextCost_inject (int cost, CallbackInfoReturnable<Integer> cir) {
		final AnvilItemCostRollupAlgorithm algorithm = AnvilItemCostRollupAlgorithm.Validator.getFromName(
				CarpetNukosSettings.anvilItemCostRollupAlgorithm
		);
		if (algorithm instanceof AnvilItemCostRollupAlgorithm.Customized) {
			cir.setReturnValue(
					((AnvilItemCostRollupAlgorithm.Customized)algorithm).getNextCost(cost)
			);
		} else if (!(algorithm instanceof AnvilItemCostRollupAlgorithm.Vanilla)) {
			ModCarpetNukos.LOGGER.warn(
					"Unknown next anvil item cost algorithm:{} : {}",
					algorithm.getClass().getName(),
					algorithm
			);
		}
	}
	
}
