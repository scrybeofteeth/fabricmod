package com.example;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.FabricLoader;
import net.minecraft.block.*;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.List;

//needs completely remade
public class Mortar extends Block {
    public static final BooleanProperty crushing = BooleanProperty.of("crushing");
    public static final IntProperty timeremaining = IntProperty.of("time_remaining",0,64);
    public static final IntProperty crusheditem = IntProperty.of("crusheditem", 0, 28);
    public static final ItemStack[] outputs = new ItemStack[28];

    public Mortar(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(crushing, false).with(crusheditem, 0).with(timeremaining, 0));
        outputs[0] = new ItemStack(Items.AIR);
        outputs[1] = new ItemStack(Items.COBBLESTONE);
        outputs[2] = new ItemStack(Items.DIORITE);
        outputs[3] = new ItemStack(Items.BOWL);

    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return VoxelShapes.cuboid(0.12f, 0f, 0.12f, 0.88f, 0.31f, 0.88f);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(crushing);
        builder.add(timeremaining);
        builder.add(crusheditem);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(player.getMainHandStack().getItem() == Items.OXEYE_DAISY){
            player.getMainHandStack().decrement(1);
            player.swingHand(Hand.MAIN_HAND);
            int duration = 10;
            world.setBlockState(pos, world.getBlockState(pos).with(crusheditem, 1).with(crushing, true).with(timeremaining, duration));
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if(world.getBlockState(pos).get(crushing) == true){
            double d = (double)pos.getX()+0.5;
            double e = (double)pos.getY()+0.2;
            double f = (double)pos.getZ()+0.5;
            world.playSound(d, e, f, SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS, 0.01F, 0.8F, true);
            world.addParticle(ParticleTypes.CLOUD, d, e, f, 0.0, 0.01f, 0.0);
            world.addParticle(ParticleTypes.CLOUD, d+0.1f, e, f, 0.0, 0.01f, 0.0);
            world.addParticle(ParticleTypes.CLOUD, d-0.1f, e, f, 0.0, 0.01f, 0.0);
            world.addParticle(ParticleTypes.CLOUD, d, e, f-0.1f, 0.0, 0.01f, 0.0);
            world.addParticle(ParticleTypes.CLOUD, d, e, f+0.1f, 0.0, 0.01f, 0.0);
            if(world.getBlockState(pos).get(timeremaining) > 0){
                int time = world.getBlockState(pos).get(timeremaining);
                world.setBlockState(pos, world.getBlockState(pos).with(timeremaining, time-1));
                world.setBlockState(pos, world.getBlockState(pos).with(crushing, true));
            }
        }
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        super.randomTick(state, world, pos, random);
        if (world.getBlockState(pos).get(timeremaining) < 0){
            world.setBlockState(pos, world.getBlockState(pos).with(crushing, false));
            if (world.getBlockState(pos).get(crusheditem) != 0){
                ItemStack stack = new ItemStack(Items.HONEYCOMB);
                ItemScatterer.spawn(world, pos, DefaultedList.copyOf(stack, stack));
                world.setBlockState(pos, world.getBlockState(pos).with(crusheditem, 0));
            }
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

}
