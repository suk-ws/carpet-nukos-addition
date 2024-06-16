package cc.sukazyo.nukos.carpet.anvils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public class AnvilContext {
	
	public final ItemStack input1;
	public final ItemStack input2;
	public final String newItemName;
	
	public final PlayerEntity player;
	
	public AnvilContext (
			ItemStack input1, ItemStack input2, String newItemName,
			PlayerEntity player
	) {
		this.input1 = input1;
		this.input2 = input2;
		this.newItemName = newItemName;
		this.player = player;
	}
	
}
