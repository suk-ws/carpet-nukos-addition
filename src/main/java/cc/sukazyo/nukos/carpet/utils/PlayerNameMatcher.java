package cc.sukazyo.nukos.carpet.utils;

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
	
}
