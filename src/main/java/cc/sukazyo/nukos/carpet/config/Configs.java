package cc.sukazyo.nukos.carpet.config;

import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class Configs {
	
	private static final Path configRoot = FabricLoader.getInstance().getConfigDir().resolve(ModCarpetNukos.MODID);
	
	public static Path getConfig (String relaPath) {
		return configRoot.resolve(relaPath);
	}
	
	public static Optional<List<String>> getScript (String fileName) {
		final var file = getConfig(fileName + ".mcfunction").toFile();
		if (file.exists() && file.isFile() && file.canRead()) {
			try (final var stream = new FileInputStream(file)) {
				final var strings = new String(stream.readAllBytes(), StandardCharsets.UTF_8);
				return Optional.of(List.of(strings.split("\\n")));
			} catch (IOException ignored) {}
		}
		return Optional.empty();
	}
	
}
