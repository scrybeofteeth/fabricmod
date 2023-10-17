package com.example;

import com.ibm.icu.impl.number.RoundingUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;

public class Stickiness extends StatusEffect {

    protected Stickiness(StatusEffectCategory category, int color) {
        super(category, color);
    }

    // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the status effect every tick.
        return true;
    }

    // This method is called when it applies the status effect. We implement custom functionality here.
    public Boolean nexttoblock = false;
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.horizontalCollision){ nexttoblock = true;}

        if (entity.forwardSpeed > 0 && nexttoblock){
            entity.move(MovementType.SELF, new Vec3d(0, 0.2f, 0));
            entity.setNoGravity(true);
            BlockPos UP = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()+1);
            BlockPos DOWN = new BlockPos(entity.getBlockX(), entity.getBlockY(), entity.getBlockZ()-1);
            BlockPos LEFT = new BlockPos(entity.getBlockX()+1, entity.getBlockY(), entity.getBlockZ());
            BlockPos RIGHT = new BlockPos(entity.getBlockX()-1, entity.getBlockY(), entity.getBlockZ());
            if (entity.getWorld().getBlockState(UP).getBlock().equals(Blocks.AIR) && entity.getWorld().getBlockState(DOWN).getBlock().equals(Blocks.AIR) && entity.getWorld().getBlockState(RIGHT).getBlock().equals(Blocks.AIR) && entity.getWorld().getBlockState(LEFT).getBlock().equals(Blocks.AIR)){
                nexttoblock = false;
                entity.setNoGravity(false);
            }
            if (entity.getStatusEffect(ExampleMod.STICKY).getDuration() == 1){
                entity.setNoGravity(false);
                nexttoblock = false;
            }

        }
    }

}