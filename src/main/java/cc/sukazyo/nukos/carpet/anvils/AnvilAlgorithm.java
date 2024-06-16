package cc.sukazyo.nukos.carpet.anvils;

import java.util.Optional;

public interface AnvilAlgorithm {
	
	Optional<AnvilResult> updateResult (AnvilContext context);
	
}
