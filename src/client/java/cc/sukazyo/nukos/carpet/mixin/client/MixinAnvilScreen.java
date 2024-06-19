package cc.sukazyo.nukos.carpet.mixin.client;

import cc.sukazyo.nukos.carpet.CarpetNukosSettings;
import cc.sukazyo.nukos.carpet.text.anvil.AnvilTextHelper;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class MixinAnvilScreen extends ForgingScreen<AnvilScreenHandler> {
	
	@Shadow
	private TextFieldWidget nameField;
	
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
