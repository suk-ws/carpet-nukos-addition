package cc.sukazyo.nukos.carpet.text.anvil;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class AnvilTextHelper {
	
	public static IAnvilTextHelper getImplementation () {
		return MyAnvilTextHelpers.getFromName(CarpetNukosSettings.anvilCustomNameSerializer);
	}
	
	public static String stripInvalidChars (String input) {
		return getImplementation().stripInvalidChars(input);
	}
	
	public static boolean itemIsOfName (ItemStack item, String inputName) {
		return getImplementation().itemIsOfName(item, inputName);
	}
	
	public static Text toText (String input) {
		return getImplementation().toText(input);
	}
	
	public static String decompose (Text input) {
		return getImplementation().decompose(input);
	}
	
	public static int textRealLength (String input) {
		return getImplementation().textRealLength(input);
	}
	
	public static void setNameToItem (ItemStack item, String input) {
		getImplementation().setNameToItem(item, input);
	}
	
}
