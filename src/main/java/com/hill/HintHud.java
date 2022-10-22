package com.hill;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

public class HintHud extends DrawableHelper {
    MinecraftClient client;
    TextRenderer textRenderer;
    private HitResult hit;
    private static final Identifier HINT_TEXTURES = new Identifier("hill", "textures/gui/hint.png");

    public HintHud(MinecraftClient client) {
        this.client = client;
        this.textRenderer = this.client.textRenderer;
    }

    public void render(MatrixStack matrices) {
        Entity entity = this.client.getCameraEntity();
        this.hit = entity.raycast(20.0, 0.0F, true);
        String string = "";
        if (hit.getType() == HitResult.Type.BLOCK) {
            string += "Block: ";
            Block blockState = this.client.world.getBlockState(((BlockHitResult) hit).getBlockPos()).getBlock();
            string += blockState.getName().getString();
            this.textRenderer.draw(matrices, string, 2, 2, 14737632);
            string = "";
            string += Formatting.GRAY;
            string += Registry.BLOCK.getId(blockState);
            this.textRenderer.draw(matrices, string, 2, 11, 14737632);
        }
    }
}
