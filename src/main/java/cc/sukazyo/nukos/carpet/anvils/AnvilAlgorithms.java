package cc.sukazyo.nukos.carpet.anvils;

import carpet.api.settings.CarpetRule;
import cc.sukazyo.nukos.carpet.anvils.algorithms.VanillaAlgorithm;
import cc.sukazyo.nukos.carpet.anvils.algorithms.VanillaReforgedAlgorithm;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

public class AnvilAlgorithms {
	
	public static Pair<AnvilAlgorithm, String> validateAndGet (String name) {
		
		return switch (name) {
			case "vanilla" -> new Pair<>(new VanillaAlgorithm(), name);
			case "vanilla-reforged" -> new Pair<>(new VanillaReforgedAlgorithm(), name);
			default -> new Pair<>(null, null);
		};
		
	}
	
	public static AnvilAlgorithm getFromName (String name) {
		return validateAndGet(name).getLeft();
	}
	
	public static class Validator extends carpet.api.settings.Validator<String> {
		
		@Override
		public String validate (
				@Nullable ServerCommandSource source, CarpetRule<String> changingRule,
				String newValue, String userInput
		) {
			return validateAndGet(userInput).getRight();
		}
		
	}
	
}
