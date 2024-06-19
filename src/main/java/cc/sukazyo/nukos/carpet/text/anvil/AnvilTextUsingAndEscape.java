package cc.sukazyo.nukos.carpet.text.anvil;

import cc.sukazyo.nukos.carpet.text.FormattedTextByAnd;
import cc.sukazyo.nukos.carpet.utils.TextDecomposer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.Objects;

public class AnvilTextUsingAndEscape implements IAnvilTextHelper {
	
	@Override
	public boolean itemIsOfName (ItemStack item, String inputName) {
		return Objects.equals(toText(inputName), item.getName());
	}
	
	@Override
	public Text toText (String input) {
		return FormattedTextByAnd.toText(input);
	}
	
	@Override
	public String decompose (Text input) {
		return FormattedTextByAnd.fromText(input);
	}
	
	@Override
	public int textRealLength (String input) {
		return TextDecomposer.getStringLength(FormattedTextByAnd.encodeAndSignal(input));
	}
	
	@Override
	public void setNameToItem (ItemStack item, String input) {
		item.setCustomName(toText(input));
	}
	
}
