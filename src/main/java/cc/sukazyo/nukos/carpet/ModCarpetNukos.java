package cc.sukazyo.nukos.carpet;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModCarpetNukos implements ModInitializer {
	
	public static final String MODID = "carpet_nukos_addition";
	public static final String NAME = "Carpet Nukos Addition";
	public static final String VERSION = BuildConfig.MOD_VERSION;
	public static Logger LOGGER = LogManager.getLogger(NAME);
	
	@Override
	public void onInitialize () {
		CarpetAdditionNukos.init();
	}
	
}
