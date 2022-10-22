package com.hill.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	private static final Text COPYRIGHT = Text.translatable("menu.copyright");
}