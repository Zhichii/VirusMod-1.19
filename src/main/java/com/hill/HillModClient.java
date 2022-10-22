package com.hill;

import com.hill.entities.Car;
import com.hill.entities.TuffGolem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;
import com.hill.entities.Helicopter;
import com.hill.entities.Virus;

@Environment(EnvType.CLIENT)
public class HillModClient implements ClientModInitializer {
    public static final EntityModelLayer MODEL_VIRUS_LAYER = new EntityModelLayer(new Identifier("hill", "virus"), "main");
    public static final EntityModelLayer MODEL_HELICOPTER_LAYER = new EntityModelLayer(new Identifier("hill", "helicopter"), "main");
    public static final EntityModelLayer MODEL_CAR_LAYER = new EntityModelLayer(new Identifier("hill", "car"), "main");
    public static final EntityModelLayer MODEL_TUFFGOLEM_LAYER = new EntityModelLayer(new Identifier("hill", "tuff_golem"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(Virus.VIRUS, (context) -> {
            return new Virus.VirusEntityRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(MODEL_VIRUS_LAYER, Virus.VirusEntityModel::getTexturedModelData);



        EntityRendererRegistry.register(Helicopter.HELICOPTER, (context) -> {
            return new Helicopter.HelicopterEntityRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(MODEL_HELICOPTER_LAYER, Helicopter.HelicopterEntityModel::getTexturedModelData);



        EntityRendererRegistry.register(Car.CAR, (context) -> {
            return new Car.CarEntityRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(MODEL_CAR_LAYER, Car.CarEntityModel::getTexturedModelData);



        EntityRendererRegistry.register(TuffGolem.TUFFGOLEM, (context) -> {
            return new TuffGolem.TuffGolemRenderer(context);
        });

        EntityModelLayerRegistry.registerModelLayer(MODEL_TUFFGOLEM_LAYER, TuffGolem.TuffGolemEntityModel::getTexturedModelData);
    }
}