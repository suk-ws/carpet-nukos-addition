package cc.sukazyo.nukos.carpet;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModCarpetNukos implements ModInitializer {
	
	public static final String MODID = "carpet_nukos_addition";
	public static final String NAME = "Carpet Nukos Addition";
	public static String VERSION;
	public static Logger LOGGER = LogManager.getLogger(NAME);
	
	@Override
	public void onInitialize () {
		VERSION = FabricLoader.getInstance().getModContainer(MODID)
						  .orElseThrow(RuntimeException::new)
						  .getMetadata().getVersion()
						  .getFriendlyString();
		CarpetAdditionNukos.init();
	}
	
}
