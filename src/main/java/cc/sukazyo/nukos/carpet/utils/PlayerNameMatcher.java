package cc.sukazyo.nukos.carpet.utils;

import net.minecraft.entity.player.PlayerEntity;

import java.util.Arrays;
import java.util.regex.Pattern;

public class PlayerNameMatcher {
	
	private final Pattern[] patterns;
	
	protected PlayerNameMatcher (String[] matchers) {
		patterns = Arrays.stream(matchers)
				.map(regex -> Pattern.compile(regex.replaceAll("\\*", ".*")))
				.toArray(Pattern[]::new);
	}
	
	public static PlayerNameMatcher of (String... matchers) {
		return new PlayerNameMatcher(matchers);
	}
	
	public static PlayerNameMatcher of (String matchers) {
		return of(matchers.split(","));
	}
	
	public boolean match (String playerName) {
		for (final var pattern : patterns) {
			if (pattern.matcher(playerName).matches())
				return true;
		}
		return false;
	}
	
	public boolean match (PlayerEntity player) {
		return match(player.getGameProfile().getName());
	}
	
	public boolean doFilter (PlayerEntity player) {
		return match(player);
	}
	
	public boolean doFilterNot (PlayerEntity player) {
		return !doFilter(player);
	}
	
}
