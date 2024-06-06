package cc.sukazyo.nukos.carpet.anvils;

import carpet.api.settings.CarpetRule;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Pair;
import org.jetbrains.annotations.Nullable;

public interface AnvilItemCostRollupAlgorithm {
	
	interface Customized extends AnvilItemCostRollupAlgorithm {
		int getNextCost (int cost);
	}
	
	record Vanilla() implements AnvilItemCostRollupAlgorithm {
		@Override public String toString () { return "vanilla"; }
	}
	
	record Linear(int additions) implements Customized {
		
		@Override public String toString () { return "linear:" + additions; }
		
		@Override
		public int getNextCost (int cost) {
			return cost + additions;
		}
		
	}
	
	record NoChange() implements Customized {
		
		@Override public String toString () { return "no-change"; }
		
		@Override
		public int getNextCost (int cost) {
			return cost;
		}
		
	}
	
	record FixedValue(int fixedValue) implements Customized {
		
		@Override public String toString () { return "fixed:" + fixedValue; }
		
		@Override
		public int getNextCost (int cost) {
			return fixedValue;
		}
		
	}
	
	class Validator extends carpet.api.settings.Validator<String> {
		
		private static Pair<AnvilItemCostRollupAlgorithm, String> validateAndGetValue (
				String userInput
		) {
			if (userInput.equals("vanilla"))
				return new Pair<>(new Vanilla(), userInput);
			else if (userInput.equals("no-change"))
				return new Pair<>(new NoChange(), userInput);
			else if (userInput.startsWith("linear:")) {
				String[] parts = userInput.split(":", 2);
				if (parts.length != 2)
					return new Pair<>(null, null);
				try {
					return new Pair<>(new Linear(Integer.parseInt(parts[1])), userInput);
				} catch (NumberFormatException e) {
					return new Pair<>(null, null);
				}
			} else if (userInput.startsWith("fixed:")) {
				String[] parts = userInput.split(":", 2);
				if (parts.length != 2)
					return new Pair<>(null, null);
				try {
					return new Pair<>(new FixedValue(Integer.parseInt(parts[1])), userInput);
				} catch (NumberFormatException e) {
					return new Pair<>(null, null);
				}
			} else
				return new Pair<>(null, null);
		}
		
		@Override
		public String validate (
				@Nullable ServerCommandSource source,
				CarpetRule<String> changingRule,
				String newValue,
				String userInput
		) {
			return validateAndGetValue(userInput).getRight();
		}
		
		public static AnvilItemCostRollupAlgorithm getFromName (String name) {
			return validateAndGetValue(name).getLeft();
		}
		
	}
	
	
}
