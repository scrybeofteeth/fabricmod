package com.example;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import org.spongepowered.include.com.google.common.base.Throwables;

import java.awt.*;

public class ExampleMod implements ModInitializer {
	// public static final Block EXAMPLE_BLOCK = new Block(FabricBlockSettings.of(Material.METAL).strength(4.0f)); // fabric api version <= 0.77.0
	public static final Mortar MORTAR  = new Mortar(FabricBlockSettings.create().strength(2.0f).nonOpaque().notSolid().sounds(BlockSoundGroup.DEEPSLATE));
	public static final TermiteMound TERMITE_MOUND = new TermiteMound(FabricBlockSettings.create().strength(0.2f).sounds(BlockSoundGroup.MUD));
	public static final TermiteBowl TERMITE_BOWL = new TermiteBowl(new FabricItemSettings().food(new FoodComponent.Builder().alwaysEdible().snack().build()).maxCount(16).recipeRemainder(Items.BOWL));
	public static final Item EXTRACT = new Item(new FabricItemSettings());
	public static final StatusEffect STICKY = new Stickiness(StatusEffectCategory.BENEFICIAL, StatusEffects.HASTE.getColor());
	public static final Alchemical_Balm BALM = new Alchemical_Balm(new FabricItemSettings().maxCount(16));
	public static final PlanterPot PLANTER_POT = new PlanterPot(FabricBlockSettings.create().nonOpaque().notSolid().sounds(BlockSoundGroup.STONE));
	public static final BlockEntityType<PlanterPotEntity> PLANTERPOT_ENTITY = Registry.register(
			Registries.BLOCK_ENTITY_TYPE,
			new Identifier("alchemic", "planter_pot_entity"),
			FabricBlockEntityTypeBuilder.create(PlanterPotEntity::new, PLANTER_POT).build()
	);
	public static final EntityType<Balm_Entity> BALM_ENTITY_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			new Identifier("alchemic", "alchemic_balm_entity"),
			FabricEntityTypeBuilder.<Balm_Entity>create(SpawnGroup.MISC, Balm_Entity::new)
					.dimensions(EntityDimensions.fixed(0.25F, 0.25F)) // dimensions in Minecraft units of the projectile
					.trackRangeBlocks(4).trackedUpdateRate(10) // necessary for all thrown projectiles (as it prevents it from breaking, lol)
					.build() // VERY IMPORTANT DONT DELETE FOR THE LOVE OF GOD PSLSSSSSS
	);

	@Override
	public void onInitialize() {
		Registry.register(Registries.BLOCK, new Identifier("alchemic", "mortar"), MORTAR);
		Registry.register(Registries.ITEM, new Identifier("alchemic", "mortar"), new BlockItem(MORTAR, new FabricItemSettings()));

		Registry.register(Registries.BLOCK, new Identifier("alchemic", "termite_mound"), TERMITE_MOUND);
		Registry.register(Registries.ITEM, new Identifier("alchemic", "termite_mound"), new BlockItem(TERMITE_MOUND, new FabricItemSettings()));

		Registry.register(Registries.ITEM, new Identifier("alchemic", "bowl_of_termites"), TERMITE_BOWL);

		Registry.register(Registries.ITEM, new Identifier("alchemic", "alchemical_extract"), EXTRACT);
		NewPotions.registerPotionsRecipes();
		Registry.register(Registries.STATUS_EFFECT, new Identifier("alchemic", "stickiness"), STICKY);
		Registry.register(Registries.ITEM, new Identifier("alchemic", "balm"), BALM);

		Registry.register(Registries.BLOCK, new Identifier("alchemic", "planter_pot"), PLANTER_POT);
		Registry.register(Registries.ITEM, new Identifier("alchemic", "planter_pot"), new BlockItem(PLANTER_POT, new FabricItemSettings()));
	}

}