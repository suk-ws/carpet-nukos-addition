package cc.sukazyo.nukos.carpet.ticks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;

import java.util.Arrays;

public class AutoTickFreezeEvents {
	
	@FunctionalInterface
	public interface TickFreezing {
		void onTickFreezing (MinecraftServer server, boolean useDeepFreeze);
	}
	
	public static final Event<TickFreezing> TICK_FREEZING = EventFactory.createArrayBacked(
			TickFreezing.class,
			callbacks -> (server, useDeepFreeze) ->
					Arrays.stream(callbacks).forEach(callback -> callback.onTickFreezing(server, useDeepFreeze))
	);
	
	@FunctionalInterface
	public interface TickUnfreezing {
		void onTickUnfreezing (MinecraftServer server);
	}
	
	public static final Event<TickUnfreezing> TICK_UNFREEZING = EventFactory.createArrayBacked(
			TickUnfreezing.class,
			callbacks -> (server) ->
					Arrays.stream(callbacks).forEach(callback -> callback.onTickUnfreezing(server))
	);
	
}
