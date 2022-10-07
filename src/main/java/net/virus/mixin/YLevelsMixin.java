package net.virus.mixin;

import net.minecraft.world.dimension.YLevels;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(YLevels.class)
public class YLevelsMixin {
    public static final int OVERWORLD_MIN_Y = -128;
    public static final int OVERWORLD_HEIGHT = 512;
}
