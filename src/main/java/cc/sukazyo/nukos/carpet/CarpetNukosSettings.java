package cc.sukazyo.nukos.carpet;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;
import cc.sukazyo.nukos.carpet.anvils.AnvilItemCostRollupAlgorithm;

public class CarpetNukosSettings {
	
	@Rule(
			categories = {CarpetAdditionNukos.CATEGORY_KEY, RuleCategory.SURVIVAL, RuleCategory.FEATURE},
			options = {"vanilla", "linear:1", "no-change", "fixed:0"},
			strict = false,
			validators = AnvilItemCostRollupAlgorithm.Validator.class
	)
	public static String anvilItemCostRollupAlgorithm = "vanilla";
	
	@Rule(
			categories = {CarpetAdditionNukos.CATEGORY_KEY, RuleCategory.OPTIMIZATION, RuleCategory.EXPERIMENTAL}
	)
	public static Boolean tickFreezeWhenNoPlayers = false;
	
}
