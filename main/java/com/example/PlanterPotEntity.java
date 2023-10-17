package com.example;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PlanterPotEntity extends BlockEntity {
    public PlanterPotEntity(BlockPos pos, BlockState state) {
        super(ExampleMod.PLANTERPOT_ENTITY, pos, state);
    }
}