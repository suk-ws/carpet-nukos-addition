package cc.sukazyo.nukos.carpet.mixin.client;

import carpet.script.external.Carpet;
import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.text.anvil.AnvilTextHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class MixinAnvilScreen extends ForgingScreen<AnvilScreenHandler> {
	
	@Shadow
	private TextFieldWidget nameField;
	@Final @Shadow
	private static Text TOO_EXPENSIVE_TEXT;
	@Final @Shadow
	private PlayerEntity player;
	
	public MixinAnvilScreen (
			AnvilScreenHandler handler, PlayerInventory playerInventory,
			Text title, Identifier texture
	) {
		super(handler, playerInventory, title, texture);
	}
	
	@Inject(method = "setup", at = @At("RETURN"))
	protected void inject_setup(CallbackInfo ci) {
		this.nameField.setMaxLength(1000);
	}
	
	@Inject(method = "drawForeground", at = @At("HEAD"), cancellable = true)
	protected void drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
		if (CarpetNukosSettings.getCurrentAnvilAlgorithm().isReforgedCostLimit()) {
			ci.cancel();
			super.drawForeground(context, mouseX, mouseY);
			
			final Slot output = this.handler.getSlot(2);
			final int cost = this.handler.getLevelCost();
			final PlayerEntity player = this.client.player;
			
			int color = 0x80ff20;
			Text text;
			// set TOO EXPENSIVE labels
			if (cost >= CarpetNukosSettings.anvilTooExpensiveLimit
					&& !player.getAbilities().creativeMode) {
				text = TOO_EXPENSIVE_TEXT;
				color = 0xff6060;
			} else if (!output.hasStack()) {
			// if there are no items then it does not need to show anything
				text = null;
			} else {
			// set normally take item labels
				text = Text.translatable("container.repair.cost", this.handler.getLevelCost());
				if (!output.canTakeItems(this.player)) {
					color = 0xff6060;
				}
			}
			
			// draw labels
			if (text != null) {
				int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(text) - 2;
				context.fill(k - 2, 67, this.backgroundWidth - 8, 79, 1325400064);
				context.drawTextWithShadow(this.textRenderer, text, k, 69, color);
			}
			
		}
	}
	
	@Inject(method = "onSlotUpdate", at = @At("HEAD"), cancellable = true)
	public void inject_onSlotUpdate(ScreenHandler handler, int slotId, ItemStack stack, CallbackInfo ci) {
		if (CarpetNukosSettings.getCurrentAnvilAlgorithm().isReforgedSetName() && slotId == 0) {
			this.nameField.setText(stack.isEmpty() ? "" : AnvilTextHelper.decompose(stack.getName()));
			this.nameField.setEditable(!stack.isEmpty());
			this.setFocused(this.nameField);
			ci.cancel();
		}
	}
	
}
