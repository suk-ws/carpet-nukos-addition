package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.utils.PlayerNameMatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Optional;

@Mixin(SleepManager.class)
public abstract class MixinSleepManager {
	
	@Shadow private int total;
	@Shadow private int sleeping;
	
	@Inject(method = "canResetTime", at = @At("HEAD"), cancellable = true)
	public void inject_canResetTime (
			int percentage, List<ServerPlayerEntity> players,
			CallbackInfoReturnable<Boolean> cir
	) {
		
		final var filter = isEnabledInject();
		if (filter.isEmpty()) return;
		
		final int availableSleepingPlayer = (int)players.stream()
				.filter(filter.get()::doFilterNot)
				.filter(PlayerEntity::canResetTimeBySleeping)
				.count();
		cir.setReturnValue(availableSleepingPlayer >= this.getNightSkippingRequirement(percentage));
		
	}
	
	@Shadow
	public abstract int getNightSkippingRequirement (int percentage);
	
	@Inject(method = "update", at = @At("HEAD"), cancellable = true)
	public void inject_update (List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir) {
		
		final var filter = isEnabledInject();
		if (filter.isEmpty()) return;
		
		final int old_total = this.total;
		final int old_sleeping = this.sleeping;
		this.total = this.sleeping = 0;
		
		players.stream()
				.filter(filter.get()::doFilterNot)
				.forEach(player -> {
					if (!player.isSpectator()) {
						this.total++;
						if (player.isSleeping()) {
							this.sleeping++;
						}
					}
				});
		
		cir.setReturnValue((old_sleeping > 0 || this.sleeping > 0) && (old_total != this.total || old_sleeping != this.sleeping));
		
	}
	
	@Unique
	private static Optional<PlayerNameMatcher> isEnabledInject () {
		if (CarpetNukosSettings.ignoringPlayersOnSleeping.isBlank()) return Optional.empty();
		else return Optional.of(PlayerNameMatcher.of(CarpetNukosSettings.ignoringPlayersOnSleeping));
	}
	
}
