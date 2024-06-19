package cc.sukazyo.nukos.carpet.text;

import cc.sukazyo.nukos.carpet.utils.TextDecomposer;
import net.minecraft.text.Text;

public class FormattedTextByAnd {
	
	public static Text toText (String input) {
		return TextDecomposer.toFormattedComponent(encodeAndSignal(input));
	}
	
	public static String fromText (Text input) {
		return decodeAndSignal(TextDecomposer.toFormattedString(input));
	}
	
	public static String decodeAndSignal (String input) {
		return input
				.replaceAll("&", "\\\\&")
				.replaceAll("§", "&");
	}
	
	public static String encodeAndSignal (String input) {
		return input
				.replaceAll("(?<!\\\\)&", (char)167 + "")
				.replaceAll("\\\\&", "&");
	}
	
}
