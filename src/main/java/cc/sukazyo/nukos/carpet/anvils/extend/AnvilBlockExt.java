package cc.sukazyo.nukos.carpet.anvils.extend;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;

public class AnvilBlockExt {
	
	public static boolean isBroken (BlockState currentState) {
		return (currentState.isOf(Blocks.DAMAGED_ANVIL) || currentState.isOf(Blocks.CHIPPED_ANVIL));
	}
	
	public static BlockState getRepairedState (BlockState currentState) {
		if (currentState.isOf(Blocks.DAMAGED_ANVIL))
			return Blocks.CHIPPED_ANVIL.getDefaultState().with(AnvilBlock.FACING, currentState.get(AnvilBlock.FACING));
		if (currentState.isOf(Blocks.CHIPPED_ANVIL))
			return Blocks.ANVIL.getDefaultState().with(AnvilBlock.FACING, currentState.get(AnvilBlock.FACING));
		return currentState;
	}
	
}
