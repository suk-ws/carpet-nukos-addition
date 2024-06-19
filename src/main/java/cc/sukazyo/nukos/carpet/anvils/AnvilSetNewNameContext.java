package cc.sukazyo.nukos.carpet.anvils;

import net.minecraft.screen.slot.Slot;

import java.util.function.Consumer;

public record AnvilSetNewNameContext (
		String newItemName,
		String oldItemName,
		Slot slot,
		Consumer<String> setOldItemName
) {
	
	public record ReturnedContext (boolean isSuccess) {}
	
}
