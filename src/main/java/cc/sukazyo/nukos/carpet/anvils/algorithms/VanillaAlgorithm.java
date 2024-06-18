package cc.sukazyo.nukos.carpet.anvils.algorithms;

import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithm;
import cc.sukazyo.nukos.carpet.anvils.AnvilContext;
import cc.sukazyo.nukos.carpet.anvils.AnvilInputContext;
import cc.sukazyo.nukos.carpet.anvils.AnvilResult;

import java.util.Optional;

public class VanillaAlgorithm implements AnvilAlgorithm {
	
	@Override
	public Optional<AnvilResult> updateResult (AnvilInputContext context) {
		return Optional.empty();
	}
	
	@Override
	public Optional<Boolean> canTakeout (AnvilContext context) {
		return Optional.empty();
	}
	
}
