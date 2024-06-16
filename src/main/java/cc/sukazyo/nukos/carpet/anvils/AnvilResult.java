package cc.sukazyo.nukos.carpet.anvils;

import net.minecraft.item.ItemStack;

public class AnvilResult {
	
	public ItemStack output = ItemStack.EMPTY;
	public int levelCost = 0;
	public int ingotUsed = 0;
	
	public AnvilResult setOutput (ItemStack output) { this.output = output; return this; }
	public AnvilResult setLevelCost (int levelCost) { this.levelCost = levelCost; return this; }
	public AnvilResult setIngotUsed (int ingotUsed) { this.ingotUsed = ingotUsed; return this; }
	
	public static AnvilResult empty () {
		return new AnvilResult();
	}
	
}
