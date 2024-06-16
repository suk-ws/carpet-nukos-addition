package cc.sukazyo.nukos.carpet.ticks;

import carpet.network.ServerNetworkHandler;
import net.minecraft.server.MinecraftServer;

/** This is for fixing the client-server sync problem when the AutoTickFreeze is enabled.
 *<p>
 * It seems that when client is joining the server and then server un-freeze immediately from
 * freezed state, the client will miss the freeze status change update, and still think server
 * is under freeze state. This class aims to dirty-fix this problem using a simple method:
 * continuously send server freezing status to client every second, so that the server will
 * almost always can know the server freezing status, no matter some update is missed.
 *
 * @see <a href="https://github.com/gnembon/fabric-carpet/issues/1924">https://github.com/gnembon/fabric-carpet/issues/1924</a>
 *
 */
public class TickStatusClientSyncThread extends Thread {
	
	public final MinecraftServer server;
	
	public TickStatusClientSyncThread (MinecraftServer server) {
		this.server = server;
	}
	
	@Override
	public void run () {
		while (true) {
			ServerNetworkHandler.updateFrozenStateToConnectedPlayers(server);
			try {
				sleep(1000);
			} catch (InterruptedException e) {
				break;
			}
		}
	}
	
}
