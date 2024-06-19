package cc.sukazyo.nukos.carpet;

import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModCarpetNukosClient implements ClientModInitializer {
	
	public static final Logger logger = LogManager.getLogger(ModCarpetNukos.NAME + " Client");
	
	@Override
	public void onInitializeClient () {
		logger.info("Hello nukos client!");
	}
	
}
