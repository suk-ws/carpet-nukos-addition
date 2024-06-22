package cc.sukazyo.nukos.carpet.anvils;

import java.util.Optional;

public interface AnvilAlgorithm {
	
	default Optional<AnvilResult> updateResult (AnvilInputContext context) {
		return Optional.empty();
	}
	
	default Optional<Boolean> canTakeout (AnvilContext context) {
		return Optional.empty();
	}
	
	default Optional<AnvilSetNewNameContext.ReturnedContext> setNewName (AnvilSetNewNameContext context) {
		return Optional.empty();
	}
	
	default boolean isReforgedSetName () {
		return false;
	}
	
	default boolean isReforgedCostLimit () {
		return false;
	}
	
}
