package com.hill.entities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.*;

public class TuffGolemBrain {

    protected static Brain<?> create(Brain<TuffGolem.TuffGolemEntity> brain) {
        addCoreActivities(brain);
        addIdleActivities(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<TuffGolem.TuffGolemEntity> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(new StayAboveWaterTask(0.8F), new WalkTask(2.5F), new LookAroundTask(45, 90), new WanderAroundTask(), new TemptationCooldownTask(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS), new TemptationCooldownTask(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)));
    }

    private static void addIdleActivities(Brain<TuffGolem.TuffGolemEntity> brain) {
        brain.setTaskList(Activity.IDLE,
                ImmutableList.of(
                        Pair.of(0, new WalkToNearestVisibleWantedItemTask((tuffgolem) -> { return true; }, 1.75F, true, 32)),
                        Pair.of(4, new RandomTask(ImmutableList.of(
                                Pair.of(new NoPenaltyStrollTask(1.0F), 2),
                                Pair.of(new GoTowardsLookTarget(1.0F, 3), 2),
                                Pair.of(new WaitTask(30, 60), 1))))),
                ImmutableSet.of());
    }

}
