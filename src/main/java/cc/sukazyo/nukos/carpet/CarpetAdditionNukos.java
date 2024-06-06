package cc.sukazyo.nukos.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import cc.sukazyo.nukos.carpet.ticks.AutoTickFreeze;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.Objects;

public class CarpetAdditionNukos implements CarpetExtension {
	
	public static final String CATEGORY_KEY = "nukos";
	public static MinecraftServer SERVER;
	
	public static void init () {
		CarpetServer.manageExtension(new CarpetAdditionNukos());
		ModCarpetNukos.LOGGER.info("Hello, Nukoland!");
	}
	
	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(CarpetNukosSettings.class);
	}
	
	@Override
	public void onServerLoaded (MinecraftServer server) {
		SERVER = server;
	}
	
	@Override
	public void onServerLoadedWorlds (MinecraftServer server) {
		AutoTickFreeze.onWorldInit();
		CarpetServer.settingsManager.registerRuleObserver((source, changedRule, userInput) -> {
			if (Objects.equals(changedRule.name(), "tickFreezeWhenNoPlayers")) {
				AutoTickFreeze.onConfigChanged((boolean)changedRule.value());
			}
		});
	}
	
	@Override
	public void onPlayerLoggedIn (ServerPlayerEntity player) {
		AutoTickFreeze.onPlayerJoin();
	}
	
	@Override
	public void onPlayerLoggedOut (ServerPlayerEntity player) {
		AutoTickFreeze.onPlayerLeave(player);
	}
	
	@Override
	public Map<String, String> canHasTranslations(String lang) {
		return Translations.getTranslationFromResourcePath("assets/carpet_nukos_addition/lang/" + lang + ".json");
	}
	
}