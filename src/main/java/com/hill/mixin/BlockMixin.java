package com.hill.mixin;

import com.hill.entities.TuffGolem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Block.class)
public class BlockMixin {
    @Overwrite
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (world.getBlockState(pos).getBlock() == Blocks.WHITE_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.ORANGE_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.MAGENTA_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.LIGHT_BLUE_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.YELLOW_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.LIME_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.PINK_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.GRAY_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.LIGHT_GRAY_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.CYAN_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.PURPLE_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.BLUE_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.BROWN_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.GREEN_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.RED_WOOL ||
                world.getBlockState(pos).getBlock() == Blocks.BLACK_WOOL
        ) {
            if (world.isInBuildLimit(pos.down())) {
                if (world.getBlockState(pos.down()).getBlock() == Blocks.TUFF) {
                    world.breakBlock(pos, false);
                    world.breakBlock(pos.down(), false);
                    Entity tuffgolem = new TuffGolem.TuffGolemEntity(TuffGolem.TUFFGOLEM, world);
                    tuffgolem.setPos(pos.getX(), pos.down().getY(), pos.getZ());
                    ((TuffGolem.TuffGolemEntity)tuffgolem).setSpawnPos(tuffgolem.getBlockPos());
                    world.spawnEntity(tuffgolem);
                }
            }
        }
    }
}