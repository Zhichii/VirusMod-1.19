package com.hill.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {
    KeyBinding zoomKey = new KeyBinding("key.zoom", 67, "key.categories.gameplay");
    int beforeZooming = 70;//((SimpleOptionMixin)MinecraftClient.getInstance().options.getFov()).getDefaultValue();
    public boolean zooming;

    @Inject(at=@At("HEAD"), method="tick(BF)V")
    public void tick(boolean slowDown, float f, CallbackInfo callbackInfo) {
        this.zooming = this.zoomKey.isPressed();
        if (this.zooming) {
            if (MinecraftClient.getInstance().options.getFov().getValue() != 30) {
                beforeZooming = MinecraftClient.getInstance().options.getFov().getValue();
            }
            MinecraftClient.getInstance().options.getFov().setValue(30);
        }
        else {
            MinecraftClient.getInstance().options.getFov().setValue(beforeZooming);
        }
    }
}
