package cc.sukazyo.nukos.carpet.pets.protect;

import java.util.Optional;

public interface PetProtectionBuilder {
	
	Optional<PetProtectionChecker> fromConfig (String config) throws IllegalConfigException;
	
	class IllegalConfigException extends Exception {
		public IllegalConfigException (String message) {
			super(message);
		}
	}
	
}
