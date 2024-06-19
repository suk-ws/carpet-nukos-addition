package cc.sukazyo.nukos.carpet.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;
import net.minecraft.util.Formatting;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.text.TextVisitFactory;

import java.util.Deque;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * This class is from mod <a href="https://github.com/Fuzss/easyanvils">Easy Anvils</a>
 *
 * @author Fuzss
 * @license MPL-2.0
 * @source <a href="https://github.com/Fuzss/easyanvils/blame/main/1.20.1/Common/src/main/java/fuzs/easyanvils/util/ComponentDecomposer.java">...</a>
 */
public class TextDecomposer {
	private static final Style EMPTY = Style.EMPTY.withColor(Formatting.WHITE.getColorValue()).withBold(false).withItalic(false).withUnderline(false).withStrikethrough(false).withObfuscated(false);
	
	public static String toFormattedString(Text component) {
		StringBuilder builder = new StringBuilder();
		component.visit((Style style, String string) -> {
			builder.append(applyLegacyFormatting(string, style));
			return Optional.empty();
		}, Style.EMPTY);
		return builder.toString();
	}
	
	private static String applyLegacyFormatting(String string, Style style) {
		return toLegacyFormatting(style).stream()
										.map(Formatting::toString)
										.reduce(String::concat)
										.map(formattings -> formattings.concat(string).concat(Formatting.RESET.toString()))
										.orElse(string);
	}
	
	private static List<Formatting> toLegacyFormatting(Style style) {
		List<Formatting> formattings = Lists.newArrayList();
		if (style.isEmpty()) return formattings;
		TextColor textColor = style.getColor();
		if (textColor != null) {
			Formatting chatFormatting = Formatting.byName(textColor.toString());
			if (chatFormatting != null) {
				formattings.add(chatFormatting);
			}
		}
		if (style.isBold()) formattings.add(Formatting.BOLD);
		if (style.isItalic()) formattings.add(Formatting.ITALIC);
		if (style.isUnderlined()) formattings.add(Formatting.UNDERLINE);
		if (style.isStrikethrough()) formattings.add(Formatting.STRIKETHROUGH);
		if (style.isObfuscated()) formattings.add(Formatting.OBFUSCATED);
		return ImmutableList.copyOf(formattings);
	}
	
	public static Text toFormattedComponent(@Nullable String value) {
		return toComponentEntries(value).stream()
										.map(entry -> Text.literal(entry.string().get()).fillStyle(entry.style()))
										.reduce(MutableText::append)
										.orElse(Text.empty());
	}
	
	public static String removeLast(@Nullable String value, int amount) {
		Deque<ComponentEntry> componentEntries = toComponentEntries(value);
		for (int i = 0; i < amount; i++) {
			ComponentEntry componentEntry = componentEntries.peekLast();
			if (componentEntry != null) {
				if (!componentEntry.string().get().isEmpty()) {
					componentEntry.string().updateAndGet(s -> s.substring(0, s.length() - 1));
				}
				if (componentEntry.string().get().isEmpty()) {
					componentEntries.pollLast();
				}
			}
		}
		return componentEntries.stream().map(entry -> applyLegacyFormatting(entry.string().get(), entry.style())).collect(Collectors.joining());
	}
	
	private static Deque<ComponentEntry> toComponentEntries(@Nullable String value) {
		Deque<ComponentEntry> values = Lists.newLinkedList();
		if (value == null) return values;
		TextVisitFactory.visitFormatted(value, EMPTY, (i, style, j) -> {
			ComponentEntry last = values.peekLast();
			if (last != null && last.style().equals(style)) {
				last.string().updateAndGet(s -> s + Character.toString(j));
			} else {
				values.offerLast(new ComponentEntry(new AtomicReference<>(Character.toString(j)), style));
			}
			return true;
		});
		return values;
	}
	
	public static int getStringLength(String value) {
		return toFormattedComponent(value).getString().length();
	}
	
	private record ComponentEntry(AtomicReference<String> string, Style style) {}
	
}
