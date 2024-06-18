package cc.sukazyo.nukos.carpet.anvils;

import java.util.Optional;

public interface AnvilAlgorithm {
	
	Optional<AnvilResult> updateResult (AnvilInputContext context);
	
	Optional<Boolean> canTakeout (AnvilContext context);
	
}
