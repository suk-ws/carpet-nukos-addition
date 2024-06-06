package cc.sukazyo.nukos.carpet.ticks;

import carpet.fakes.MinecraftServerInterface;
import carpet.helpers.ServerTickRateManager;
import cc.sukazyo.nukos.carpet.CarpetAdditionNukos;
import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import net.minecraft.server.network.ServerPlayerEntity;

public class AutoTickFreeze {
	
	public static ServerTickRateManager trm;
	
	public static void onWorldInit () {
		
		trm = ((MinecraftServerInterface)CarpetAdditionNukos.SERVER).getTickRateManager();
		
		if (!CarpetAdditionNukos.SERVER.isDedicated() && CarpetNukosSettings.tickFreezeWhenNoPlayers)
			ModCarpetNukos.LOGGER.warn("Your server is not dedicated server, tickFreezeWhenNoPlayer will not work!");
		if (isEnabled())
			checkStatus();
		
	}
	
	public static void onPlayerJoin () {
		if (isEnabled())
			tickContinue();
	}
	
	public static void onPlayerLeave (ServerPlayerEntity player) {
		if (isEnabled()) {
			if (CarpetAdditionNukos.SERVER.getCurrentPlayerCount() == 1 && CarpetAdditionNukos.SERVER.getPlayerNames()[0].equals(player.getName().getString())) {
				tickFreeze();
			} else if (CarpetAdditionNukos.SERVER.getCurrentPlayerCount() == 0) {
				tickFreeze();
			}
		}
	}
	
	public static void onConfigChanged (boolean newConfig) {
		if (isEnabled())
			checkStatus();
	}
	
	private static void checkStatus () {
		if (CarpetAdditionNukos.SERVER.getCurrentPlayerCount() == 0) {
			tickFreeze();
		} else {
			tickContinue();
		}
	}
	
	private static boolean isEnabled () {
		if (!CarpetAdditionNukos.SERVER.isDedicated())
			return false;
		return CarpetNukosSettings.tickFreezeWhenNoPlayers;
	}
	
	private static void tickFreeze () {
		if (trm.gameIsPaused()) return;
		trm.setFrozenState(true, false);
		ModCarpetNukos.LOGGER.info("Currently no player is online, server will freeze world updates.");
		ModCarpetNukos.LOGGER.info("paused ticks at tick {}", CarpetAdditionNukos.SERVER.getOverworld().getTime());
	}
	
	private static void tickContinue () {
		if (!trm.gameIsPaused()) return;
		ModCarpetNukos.LOGGER.info("Currently players are online, server will continue ticks.");
		ModCarpetNukos.LOGGER.info("starting ticks from tick {}", CarpetAdditionNukos.SERVER.getOverworld().getTime());
		trm.setFrozenState(false, false);
	}
	
}
