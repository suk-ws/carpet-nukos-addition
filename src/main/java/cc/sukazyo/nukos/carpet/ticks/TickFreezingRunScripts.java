package cc.sukazyo.nukos.carpet.ticks;

import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import cc.sukazyo.nukos.carpet.config.Configs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class TickFreezingRunScripts
implements AutoTickFreezeEvents.TickFreezing, AutoTickFreezeEvents.TickUnfreezing, CommandOutput {
	
	public ServerCommandSource getFakeConsole (MinecraftServer server) {
		final var serverWorld = server.getOverworld();
		return new ServerCommandSource(
				this,
				serverWorld == null ? Vec3d.ZERO : Vec3d.of(serverWorld.getSpawnPos()), Vec2f.ZERO, serverWorld, 4,
				"Nukos_Automatics", Text.literal("Nukos_Automatics"),
				server, null
		);
	}
	
	@Override
	public void onTickFreezing (MinecraftServer server, boolean useDeepFreeze) {
		final var commandSource = getFakeConsole(server);
		final var eventName = "on-tick-freeze";
		final var commands = Configs.getScript(eventName);
		if (commands.isEmpty()) return;
		commands.get().forEach(command -> {
			commandSource.sendMessage(Text.literal("Executing automatic script '%s' command: /%s".formatted(eventName, command)));
			server.getCommandManager().executeWithPrefix(commandSource, command);
		});
	}
	
	@Override
	public void onTickUnfreezing (MinecraftServer server) {
		final var commandSource = getFakeConsole(server);
		final var eventName = "on-tick-unfreeze";
		final var commands = Configs.getScript(eventName);
		if (commands.isEmpty()) return;
		commands.get().forEach(command -> {
			commandSource.sendMessage(Text.literal("Executing automatic script '%s' command: /%s".formatted(eventName, command)));
			server.getCommandManager().executeWithPrefix(commandSource, command);
		});
	}
	
	@Override
	public void sendMessage (Text message) {
		ModCarpetNukos.LOGGER.info("{}", message.getString());
	}
	@Override public boolean shouldReceiveFeedback () { return true; }
	@Override public boolean shouldTrackOutput () { return true; }
	@Override public boolean shouldBroadcastConsoleToOps () { return true; }
	
}
