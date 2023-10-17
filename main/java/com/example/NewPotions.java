package com.example;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NewPotions {

    public static final Potion STICKY_POTION = Registry.register(Registries.POTION, new Identifier("alchemic", "sticky_potion"), new Potion(new StatusEffectInstance(ExampleMod.STICKY, 3600, 0)));
    public static void registerPotionsRecipes(){

        BrewingRecipeRegistry.registerPotionRecipe(Potions.AWKWARD, Items.HONEYCOMB, NewPotions.STICKY_POTION);

    }

}