package cc.sukazyo.nukos.carpet;

import carpet.api.settings.Rule;
import carpet.api.settings.RuleCategory;
import cc.sukazyo.nukos.carpet.anvils.AnvilAlgorithms;
import cc.sukazyo.nukos.carpet.anvils.AnvilItemCostRollupAlgorithm;
import cc.sukazyo.nukos.carpet.CarpetAdditionNukos.NukosCategoryKeys;

public class CarpetNukosSettings {
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"vanilla", "linear:1", "no-change", "fixed:0"},
			strict = false,
			validators = AnvilItemCostRollupAlgorithm.Validator.class
	)
	public static String anvilItemCostRollupAlgorithm = "vanilla";
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"vanilla", "vanilla-reforged"},
			strict = false,
			validators = AnvilAlgorithms.Validator.class
	)
	public static String anvilAlgorithm = "vanilla";
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL})
	public static Boolean anvilUseRenameCost = true;
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL})
	public static Boolean anvilUseItemCost = true;
	
	@Rule(
			categories = {NukosCategoryKeys.NUKOS, RuleCategory.SURVIVAL, NukosCategoryKeys.ANVIL},
			options = {"40", "255"},
			strict = false
	)
	public static int anvilTooExpensiveLimit = 40;
	
	@Rule(categories = {NukosCategoryKeys.NUKOS, RuleCategory.OPTIMIZATION, RuleCategory.EXPERIMENTAL, NukosCategoryKeys.TICK})
	public static Boolean tickFreezeWhenNoPlayers = false;
	
}
