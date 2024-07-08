package cc.sukazyo.nukos.carpet.ticks;

import carpet.CarpetServer;
import carpet.fakes.MinecraftServerInterface;
import carpet.helpers.ServerTickRateManager;
import cc.sukazyo.nukos.carpet.CarpetAdditionNukos;
import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import cc.sukazyo.nukos.carpet.utils.PlayerNameMatcher;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.Set;

public class AutoTickFreeze {
	
	public static ServerTickRateManager trm;
	
	private static final Set<String> SETTINGS_NAMES = Set.of(
			"tickFreezeWhenNoPlayers",
			"tickFreezeWhenNoPlayersUseDeepFreeze",
			"ignoringPlayersOnAutoTickFreeze"
	);
	
	public static void onWorldInit () {
		
		trm = ((MinecraftServerInterface)CarpetAdditionNukos.SERVER).getTickRateManager();
		
		CarpetServer.settingsManager.registerRuleObserver((source, changedRule, userInput) -> {
			if (SETTINGS_NAMES.contains(changedRule.name())) {
				AutoTickFreeze.onConfigChanged();
			}
		});
		
		if (!CarpetAdditionNukos.SERVER.isDedicated() && CarpetNukosSettings.tickFreezeWhenNoPlayers)
			ModCarpetNukos.LOGGER.warn("Your server is not dedicated server, tickFreezeWhenNoPlayer will not work!");
		updateStatus();
		
	}
	
	public static boolean isEnabled () {
		if (!CarpetAdditionNukos.SERVER.isDedicated())
			return false;
		return CarpetNukosSettings.tickFreezeWhenNoPlayers;
	}
	
	public static void onPlayerJoin () {
		updateStatus();
	}
	
	public static void onPlayerLeave (ServerPlayerEntity leavingPlayer) {
		updateStatus(leavingPlayer.getName().getString());
	}
	
	public static void onConfigChanged () {
		updateStatus();
	}
	
	public static void updateStatus () { updateStatus(null); }
	public static void updateStatus (String ignoringPlayerName) {
		if (!isEnabled()) return;
		final var currentPlayers = getCurrentPlayers();
		if (ignoringPlayerName == null)
			ModCarpetNukos.LOGGER.debug("Current active players for server ticking {}", Arrays.toString(currentPlayers));
		else ModCarpetNukos.LOGGER.debug("Current active players for server ticking {} without leaving player [{}]", Arrays.toString(currentPlayers), ignoringPlayerName);
		if (currentPlayers.length == 1 && currentPlayers[0].equals(ignoringPlayerName))
			tickFreeze();
		else if (currentPlayers.length == 0)
			tickFreeze();
		else
			tickContinue();
	}
	
	private static String[] getCurrentPlayers () {
		return Arrays.stream(CarpetAdditionNukos.SERVER.getPlayerNames())
				.filter(x -> !PlayerNameMatcher.of(CarpetNukosSettings.ignoringPlayersOnAutoTickFreeze).match(x))
				.toArray(String[]::new);
	}
	
	private static void tickFreeze () {
		if (trm.gameIsPaused()) {
			if (trm.deeplyFrozen() && CarpetNukosSettings.tickFreezeWhenNoPlayersUseDeepFreeze) return;
			if (!trm.deeplyFrozen() && !CarpetNukosSettings.tickFreezeWhenNoPlayersUseDeepFreeze) return;
		}
		trm.setFrozenState(true, CarpetNukosSettings.tickFreezeWhenNoPlayersUseDeepFreeze);
		if (CarpetNukosSettings.tickFreezeWhenNoPlayersUseDeepFreeze)
			ModCarpetNukos.LOGGER.info("Currently no player is online, server will DEEP FREEZE world updates.");
		else ModCarpetNukos.LOGGER.info("Currently no player is online, server will freeze world updates.");
		ModCarpetNukos.LOGGER.info("paused ticks at tick {}", CarpetAdditionNukos.SERVER.getOverworld().getTime());
	}
	
	private static void tickContinue () {
		if (!trm.gameIsPaused()) return;
		ModCarpetNukos.LOGGER.info("Currently players are online, server will continue ticks.");
		ModCarpetNukos.LOGGER.info("starting ticks from tick {}", CarpetAdditionNukos.SERVER.getOverworld().getTime());
		trm.setFrozenState(false, false);
	}
	
}
