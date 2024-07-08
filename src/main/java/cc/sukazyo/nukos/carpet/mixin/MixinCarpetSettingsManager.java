package cc.sukazyo.nukos.carpet.mixin;

import carpet.CarpetServer;
import carpet.api.settings.SettingsManager;
import carpet.utils.Messenger;
import carpet.utils.Translations;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SettingsManager.class)
public class MixinCarpetSettingsManager {
	
	@Inject(
			method = "listAllSettings",
			slice = @Slice(
					from = @At(
							value = "CONSTANT",
							args = "stringValue=ui.version",
							ordinal = 0
					)
			),
			at = @At(
					value = "INVOKE",
					target = "Lcarpet/api/settings/SettingsManager;getCategories()Ljava/lang/Iterable;",
					ordinal = 0
			)
	)
	public void inject_listAlLSettings (ServerCommandSource source, CallbackInfoReturnable<Integer> cir) {
		SettingsManager self = (SettingsManager)(Object)this;
		if (self != CarpetServer.settingsManager) return;
		source.sendFeedback(() -> Messenger.c(
				"g %s ".formatted(ModCarpetNukos.NAME),
				"g %s: ".formatted(Translations.tr("nukos.versioning")),
				"g %s".formatted(ModCarpetNukos.VERSION)
		), false);
	}
	
}
