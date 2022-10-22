package com.hill.entities;

import com.google.common.collect.ImmutableList;
import com.hill.HillModClient;
import com.mojang.serialization.Dynamic;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AllayBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class TuffGolem implements ModInitializer {

    @Override
    public void onInitialize() {}

    public static class TuffGolemEntity extends PathAwareEntity implements InventoryOwner {

        private BlockPos spawnPos;
        private ItemEntity wantedItem;
        protected static final ImmutableList<SensorType<? extends Sensor<? super TuffGolemEntity>>> SENSORS = ImmutableList.of(SensorType.NEAREST_ITEMS);;
        protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.HURT_BY, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING, new MemoryModuleType[0]);


        private final SimpleInventory inventory = new SimpleInventory(1);

        public TuffGolemEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
            super(entityType, world);
            this.jumpControl = new JumpControl(this);
            //this.setMovementSpeed(0.2F);
        }

        @Override
        protected void initGoals() {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(1, new EscapeDangerGoal(this, 0.5F));
            this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.5F));
            this.goalSelector.add(4, new GoToWalkTargetGoal(this, 0.5F));
            this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 5.0F));
            this.goalSelector.add(7, new LookAroundGoal(this));
        }

        public void tickMovement() {
            super.tickMovement();
            if (this.spawnPos != null)
                if (this.getBlockPos() != this.spawnPos &&
                        this.getBlockPos().up() != this.spawnPos &&
                        this.getBlockPos().down() != this.spawnPos &&
                        this.getBlockPos().east() != this.spawnPos &&
                        this.getBlockPos().west() != this.spawnPos &&
                        this.getBlockPos().north() != this.spawnPos &&
                        this.getBlockPos().south() != this.spawnPos
                ) {
                    SensorType.NEAREST_ITEMS.create().tick(this.getServer().getWorld(this.getWorld().getRegistryKey()), this);
                    if (!this.getInventory().getStack(0).isEmpty()) {
                        this.getNavigation().startMovingTo(this.spawnPos.getX(), this.spawnPos.getY(), this.spawnPos.getZ(), 0.5F);
                    }
                }

        }

        public boolean canGather(ItemStack stack) {
            ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
            return !itemStack.isEmpty() && itemStack.isItemEqual(stack) && this.inventory.canInsert(stack) && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING);
        }

        @Override
        public SimpleInventory getInventory() {
            return this.inventory;
        }

        public void setSpawnPos(BlockPos spawnPos) {
            this.spawnPos = spawnPos;
        }

        protected Brain.Profile<TuffGolemEntity> createBrainProfile() {
            return Brain.createProfile(MEMORY_MODULES, SENSORS);
        }

        @Override
        protected EntityNavigation createNavigation(World world) {
            MobNavigation navigation = new MobNavigation(this, world);
            navigation.setCanPathThroughDoors(false);
            navigation.setCanSwim(true);
            navigation.setCanEnterOpenDoors(true);
            return navigation;
        }

        public void writeCustomDataToNbt(NbtCompound nbt) {
            super.writeCustomDataToNbt(nbt);
            nbt.put("Inventory", this.inventory.toNbtList());
            nbt.putFloat("spawnX", this.spawnPos.getX());
            nbt.putFloat("spawnY", this.spawnPos.getY());
            nbt.putFloat("spawnZ", this.spawnPos.getZ());
        }

        public void readCustomDataFromNbt(NbtCompound nbt) {
            super.readCustomDataFromNbt(nbt);
            this.inventory.setStack(0, ItemStack.fromNbt(nbt.getList("Inventory", 10).getCompound(0)));
            if (nbt.contains("spawnZ"))
                this.spawnPos = new BlockPos(nbt.getFloat("spawnX"), nbt.getFloat("spawnY"), nbt.getFloat("spawnZ"));
        }

        protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
            return TuffGolemBrain.create(this.createBrainProfile().deserialize(dynamic));
        }

    }

    public static EntityType<TuffGolemEntity> TUFFGOLEM = Registry.register(
            Registry.ENTITY_TYPE,
            new Identifier("hill", "tuff_golem"),
            EntityType.Builder.create(TuffGolemEntity::new, SpawnGroup.MISC).setDimensions(0.875F, 0.875F).build("tuff_golem")
        );

    public static class TuffGolemEntityModel extends EntityModel<TuffGolemEntity> {

        public final ModelPart body;
        public final ModelPart handL;
        public final ModelPart handR;
        public final ModelPart nose;
        public final ModelPart legL;
        public final ModelPart legR;

        public TuffGolemEntityModel(ModelPart modelPart) {
            this.body = modelPart.getChild("body");
            this.handL = modelPart.getChild("handL");
            this.handR = modelPart.getChild("handR");
            this.nose = modelPart.getChild("nose");
            this.legL = modelPart.getChild("legL");
            this.legR = modelPart.getChild("legR");
        }

        @Override
        public void setAngles(TuffGolemEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {}

        @Override
        public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
            ImmutableList.of(this.body,
                    this.handL,
                    this.handR,
                    this.nose,
                    this.legL,
                    this.legR
            ).forEach((modelRenderer) -> {
                modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
            });
        }

        public static TexturedModelData getTexturedModelData() {
            ModelData modelData = new ModelData();
            ModelPartData modelPartData = modelData.getRoot();
            float move = 1.5F;
            modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-6.5F, move+0F, -1F, 13F, 24F, 6F), ModelTransform.pivot(0F, -10F, 0F));
            modelPartData.addChild("handL", ModelPartBuilder.create().uv(0, 44).cuboid(-11.5F, move+9F, 1F, 5F, 13F, 2F), ModelTransform.pivot(0F, -10F, 0F));
            modelPartData.addChild("handR", ModelPartBuilder.create().uv(50, 0).cuboid(6.5F, move+9F, 1F, 5F, 13F, 2F), ModelTransform.pivot(0F, -10F, 0F));
            modelPartData.addChild("nose", ModelPartBuilder.create().uv(0, 30).cuboid(-1F, move+9F, -2F, 2F, 3F, 1F), ModelTransform.pivot(0F, -10F, 0F));
            modelPartData.addChild("legL", ModelPartBuilder.create().uv(0, 34).cuboid(-5.5F, move+24F, 0F, 5F, 2F, 3F), ModelTransform.pivot(0F, -10F, 0F));
            modelPartData.addChild("legR", ModelPartBuilder.create().uv(0, 39).cuboid(0.5F, move+24F, 0F, 5F, 2F, 3F), ModelTransform.pivot(0F, -10F, 0F));
            return TexturedModelData.of(modelData, 64, 64);
        }

    }

    public static class TuffGolemRenderer extends MobEntityRenderer<TuffGolemEntity, TuffGolemEntityModel> {

        public TuffGolemRenderer(EntityRendererFactory.Context context) {
            super(context, new TuffGolemEntityModel(context.getPart(HillModClient.MODEL_TUFFGOLEM_LAYER)), 0.5f);
        }

        @Override
        public Identifier getTexture(TuffGolemEntity entity) {
            return new Identifier("hill", "textures/entity/tuff_golem.png");
        }
    }
}