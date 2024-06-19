package cc.sukazyo.nukos.carpet.text.anvil;

import carpet.api.settings.CarpetRule;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

public class MyAnvilTextHelpers {
	
	public static Pair<IAnvilTextHelper, String> validateAndGet (String name) {
		return switch (name) {
			case "vanilla" -> new Pair<>(new AnvilTextVanilla(), "vanilla");
			case "escaped" -> new Pair<>(new AnvilTextVanillaAllowEscape(), "escaped");
			case "escape-and" -> new Pair<>(new AnvilTextUsingAndEscape(), "escape-and");
			case "mini-message" -> new Pair<>(new AnvilTextUsingMiniMessage(), "mini-message");
			default -> new Pair<>(null, null);
		};
	}
	
	public static IAnvilTextHelper getFromName (String name) {
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
