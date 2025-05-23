package cc.sukazyo.nukos.carpet;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.utils.Translations;
import cc.sukazyo.nukos.carpet.ticks.AutoTickFreeze;
import cc.sukazyo.nukos.carpet.ticks.TickStatusClientSyncThread;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;

public class CarpetAdditionNukos implements CarpetExtension {
	
	public static class NukosCategoryKeys {
		public static final String NUKOS = "nukos";
		public static final String TICK = "tick";
		public static final String ANVIL = "anvil";
		public static final String ENTITY = "entity";
		public static final String PETS = "pets";
	}
	
	public static MinecraftServer SERVER;
	private static TickStatusClientSyncThread tickStatusClientSyncThread;
	
	public static void init () {
		CarpetServer.manageExtension(new CarpetAdditionNukos());
		ModCarpetNukos.LOGGER.info("Hello, Nukoland v%s!".formatted(ModCarpetNukos.VERSION));
		ModCarpetNukos.LOGGER.debug("Nukoland in debugging mode!");
	}
	
	@Override
	public void onGameStarted() {
		CarpetServer.settingsManager.parseSettingsClass(CarpetNukosSettings.class);
	}
	
	@Override
	public void onServerLoaded (MinecraftServer server) {
		SERVER = server;
		tickStatusClientSyncThread = new TickStatusClientSyncThread(server);
		tickStatusClientSyncThread.start();
	}
	
	@Override
	public void onServerLoadedWorlds (MinecraftServer server) {
		AutoTickFreeze.onWorldInit();
	}
	
	@Override
	public void onServerClosed (MinecraftServer server) {
		tickStatusClientSyncThread.interrupt();
		tickStatusClientSyncThread = null;
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