package com.hill.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Objects;
import java.util.Optional;

@Mixin(SimpleOption.class)
public interface SimpleOptionMixin<T> {
    @Accessor
    T getDefaultValue();
}
    /*
    T value;
    @Overwrite
    public void setValue(T value) {
        T object = Optional.of(value).get();
        if (!MinecraftClient.getInstance().isRunning()) {
            this.value = object;
        } else {
            if (!Objects.equals(this.value, object)) {
                this.value = object;
                //MinecraftClient.getInstance().worldRenderer.scheduleTerrainUpdate().accept(this.value);
            }

        }
    }
}
//*/