package com.example;

import com.ibm.icu.message2.Mf2DataModel;
import net.fabricmc.loader.impl.util.log.LogLevel;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Colors;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;


public class TermiteMound extends Block {
    public static final BooleanProperty FULL = BooleanProperty.of("full");
    public static final IntProperty GrowthStatus = IntProperty.of("growthstatus",0,7);
    public static final IntProperty StatusFlavor = IntProperty.of("statusflavor",0,20);
    public TermiteMound(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(FULL, false).with(GrowthStatus, 0).with(StatusFlavor, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FULL);
        builder.add(GrowthStatus);
        builder.add(StatusFlavor);

    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            //scoop up termites
            if (player.getStackInHand(Hand.MAIN_HAND).getItem().equals(Items.BOWL) && world.getBlockState(pos).get(FULL).equals(true)){
                player.getStackInHand(Hand.MAIN_HAND).decrement(1);

                ItemStack itemStack = new ItemStack(ExampleMod.TERMITE_BOWL);
                //text
                List<Text> Tooltiptextfull = new ArrayList<Text>(8);
                Collections.addAll(Tooltiptextfull, Text.literal("revokes fire resistance"));


                itemStack.getItem().appendTooltip(itemStack, world, Tooltiptextfull, TooltipContext.ADVANCED);

                //nbt
                NbtCompound nbt = new NbtCompound();
                itemStack.getOrCreateSubNbt("alchemic").putInt("flavor", world.getBlockState(pos).get(StatusFlavor) );

                player.giveItemStack(itemStack);
                world.setBlockState(pos, getDefaultState());
            }
            //feed termites paper
            if (player.getStackInHand(Hand.MAIN_HAND).getItem().equals(Items.PAPER)){
                player.getStackInHand(Hand.MAIN_HAND).decrement(1);
                int rng = Random.create().nextBetween(1, 4);
                if (rng == 4){
                    world.setBlockState(pos, world.getBlockState(pos).with(GrowthStatus, world.getBlockState(pos).get(GrowthStatus) + 1));
                }
            }

            //feed termites other items
            if (player.getStackInHand(Hand.MAIN_HAND).getItem().equals(ExampleMod.EXTRACT)){
                int potionID = player.getStackInHand(Hand.MAIN_HAND).getNbt().getInt("potionID");
                player.getStackInHand(Hand.MAIN_HAND).decrement(1);
                world.setBlockState(pos, world.getBlockState(pos).with(StatusFlavor, potionID));
            }
            return ActionResult.CONSUME;
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.scheduledTick(state, world, pos, random);
        //growth
        if (world.getBlockState(pos).get(GrowthStatus) == 7){
            world.setBlockState(pos, world.getBlockState(pos).with(FULL, true));
        } else {
            world.setBlockState(pos, getDefaultState().with(GrowthStatus, world.getBlockState(pos).get(GrowthStatus) + 1));
        }

        //spreading to adjacent coarse dirt
        if (world.isRaining()){
            if(world.getBlockState(pos).get(GrowthStatus) == 7){
                BlockPos pos2 = new BlockPos(pos.getX()+Random.create().nextBetween(-1, 1), pos.getY()+Random.create().nextBetween(-1, 1), pos.getZ()+Random.create().nextBetween(-1, 1));
                if (world.getBlockState(pos2).getBlock().equals(Blocks.COARSE_DIRT) || world.getBlockState(pos2).getBlock().equals(Blocks.GRASS_BLOCK)){
                    world.setBlockState(pos2, world.getBlockState(pos));
                }
            }
        }

    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        double d = (double)pos.getX()+0.5;
        double e = (double)pos.getY()+0.2;
        double f = (double)pos.getZ()+0.5;
        if(world.getBlockState(pos).get(StatusFlavor) != 0){
            for (int i = 0; i < 4; i++){
                ParticleEffect particleEffect = ParticleTypes.EFFECT;
                world.addParticle(ParticleTypes.EFFECT, d, e, f, 0.0+Random.create().nextBetween(-1, 1), 0.01+Random.create().nextBetween(-1, 1), 0.0+Random.create().nextBetween(-1, 1));
            }
        }
    }

}
