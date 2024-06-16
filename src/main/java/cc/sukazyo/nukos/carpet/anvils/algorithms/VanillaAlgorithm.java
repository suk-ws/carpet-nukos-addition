package cc.sukazyo.nukos.carpet.anvils.algorithms;

import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithm;
import cc.sukazyo.nukos.carpet.anvils.AnvilContext;
import cc.sukazyo.nukos.carpet.anvils.AnvilResult;

import java.util.Optional;

public class VanillaAlgorithm implements AnvilAlgorithm {
	
	@Override
	public Optional<AnvilResult> updateResult (AnvilContext context) {
		return Optional.empty();
	}
	
}
