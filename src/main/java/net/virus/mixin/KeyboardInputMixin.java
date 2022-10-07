package net.virus.mixin;

import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import net.virus.VirusMain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    KeyBinding zoomKey = new KeyBinding("key.zoom", 67, "key.categories.gameplay");
    public boolean zooming;

    @Inject(at=@At("HEAD"), method="tick(BF)V")
    public void tick(boolean slowDown, float f, CallbackInfo callbackInfo) {
        this.zooming = this.zoomKey.isPressed();
        if (this.zooming) VirusMain.LOGGER.info("Zomming! ");
    }
}
