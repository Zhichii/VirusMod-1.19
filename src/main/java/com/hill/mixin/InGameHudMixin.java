package com.hill.mixin;

import com.hill.HintHud;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    HintHud hintHud = new HintHud(MinecraftClient.getInstance());

    @Inject(at = @At("HEAD"), method = "render(Lnet/minecraft/client/util/math;F)V")
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo callbackInfo){
        this.hintHud.render(matrices);
    }
}
