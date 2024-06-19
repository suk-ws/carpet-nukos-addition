package cc.sukazyo.nukos.carpet.text.anvil;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class AnvilTextUsingMiniMessage implements IAnvilTextHelper {
	
	MiniMessage builder = MiniMessage.builder()
			.tags(TagResolver.builder()
					.resolver(StandardTags.color())
					.resolver(StandardTags.reset())
					.resolver(StandardTags.decorations())
					.resolver(StandardTags.gradient())
					.resolver(StandardTags.transition())
					.resolver(StandardTags.rainbow())
					.resolver(StandardTags.translatable())
					.resolver(StandardTags.translatableFallback())
					.resolver(StandardTags.keybind())
					.build()
			)
			.build();
	
	@Override
	public boolean itemIsOfName (ItemStack item, String inputName) {
		return false;
	}
	
	@Override
	public Text toText (String input) {
		return Text.Serializer.fromJson(JSONComponentSerializer.json().serialize(builder.deserialize(input)));
	}
	
	@Override
	public String decompose (Text input) {
		return builder.serialize(JSONComponentSerializer.json().deserialize(Text.Serializer.toJson(input)));
	}
	
	@Override
	public int textRealLength (String input) {
		return toText(input).getString().length();
	}
	
	@Override
	public void setNameToItem (ItemStack item, String input) {
		item.setCustomName(toText(input));
	}
	
}
