package cc.sukazyo.nukos.carpet.text.anvil;

import net.minecraft.SharedConstants;

public class AnvilTextVanilla extends AnvilTextVanillaAllowEscape {
	
	@Override
	public String stripInvalidChars (String input) {
		return SharedConstants.stripInvalidChars(input);
	}
	
}
