package cc.sukazyo.nukos.carpet.mixin;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.ModCarpetNukos;
import cc.sukazyo.nukos.carpet.anvils.extend.AnvilBlockExt;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FallingBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilBlock.class)
public abstract class MixinAnvilBlock extends FallingBlock {
	public MixinAnvilBlock (Settings settings) { super(settings); }
	
	@Inject(method = "onUse", at = @At("HEAD"), cancellable = true)
	public void inject_onUse (
			BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit,
			CallbackInfoReturnable<ActionResult> cir
	) {
		if (!CarpetNukosSettings.anvilCanRepairUseIronBlock) return;
		if (world.isClient) return;
		ItemStack item = player.getStackInHand(hand);
		if (AnvilBlockExt.isBroken(state) && item.isOf(Blocks.IRON_BLOCK.asItem())) {
			ModCarpetNukos.LOGGER.info("fixed anvil!!!");
			world.setBlockState(pos, AnvilBlockExt.getRepairedState(state), 2);
			world.playSound(null, pos, SoundEvents.BLOCK_ANVIL_USE, SoundCategory.BLOCKS, 1f, 1f);
			if (!player.getAbilities().creativeMode)
				item.decrement(1);
			player.increaseStat(Stats.USED.getOrCreateStat(Blocks.IRON_BLOCK.asItem()), 1);
			cir.setReturnValue(ActionResult.CONSUME);
		}
	}
	
}
