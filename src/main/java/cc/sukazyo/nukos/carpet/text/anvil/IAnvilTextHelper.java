package cc.sukazyo.nukos.carpet.text.anvil;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface IAnvilTextHelper {
	
	default String stripInvalidChars (String input) {
		StringBuilder str = new StringBuilder();
		for (char ch : input.toCharArray())
			if ((ch >= ' ') && (ch != 127))
				str.append(ch);
		return str.toString();
	}
	
	boolean itemIsOfName (ItemStack item, String inputName);
	
	Text toText (String input);
	
	String decompose (Text input);
	
	int textRealLength (String input);
	
	void setNameToItem (ItemStack item, String input);
	
}
