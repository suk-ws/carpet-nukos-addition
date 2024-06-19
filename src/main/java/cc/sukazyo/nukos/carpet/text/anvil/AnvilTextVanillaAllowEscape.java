package cc.sukazyo.nukos.carpet.text.anvil;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class AnvilTextVanillaAllowEscape implements IAnvilTextHelper {
	
	@Override
	public boolean itemIsOfName (ItemStack item, String inputName) {
		return Objects.equals(inputName, item.getName().getString());
	}
	
	@Override
	public Text toText (String input) {
		return Text.literal(input);
	}
	
	@Override
	public String decompose (Text input) {
		return input.getString();
	}
	
	@Override
	public int textRealLength (String input) {
		return input.length();
	}
	
	@Override
	public void setNameToItem (ItemStack item, String input) {
		item.setCustomName(Text.literal(input));
	}
	
}
