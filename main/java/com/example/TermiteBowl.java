//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtInt;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.apache.commons.lang3.stream.Streams;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;

public class TermiteBowl extends Item {
    private static final int MAX_USE_TIME = 12;
    public static StatusEffect[] STATUS = new StatusEffect[20];
    public TermiteBowl(Item.Settings settings) {
        super(settings);
    }

    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof ServerPlayerEntity serverPlayerEntity) {
            Criteria.CONSUME_ITEM.trigger(serverPlayerEntity, stack);
            serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        }

        if (user instanceof PlayerEntity && !((PlayerEntity)user).getAbilities().creativeMode) {
            stack.decrement(1);
        }

        if (!world.isClient) {
            STATUS[0] = StatusEffects.POISON;
            STATUS[1] = StatusEffects.FIRE_RESISTANCE;
            STATUS[2] = StatusEffects.SLOWNESS;
            STATUS[3] = StatusEffects.WEAKNESS;
            STATUS[4] = StatusEffects.STRENGTH;
            STATUS[5] = StatusEffects.WATER_BREATHING;
            STATUS[6] = StatusEffects.SPEED;
            STATUS[7] = StatusEffects.SLOW_FALLING;
            STATUS[8] = StatusEffects.JUMP_BOOST;
            STATUS[9] = StatusEffects.INVISIBILITY;
            STATUS[10] = StatusEffects.LUCK;
            STATUS[11] = StatusEffects.REGENERATION;
            STATUS[12] = StatusEffects.LUCK;
            STATUS[13] = StatusEffects.NIGHT_VISION;
            STATUS[14] = StatusEffects.RESISTANCE;

            int statusnumber = stack.getSubNbt("alchemic").getInt("flavor");
            user.removeStatusEffect(STATUS[statusnumber]);
        }

        return stack.isEmpty() ? new ItemStack(Items.BOWL) : stack;
    }

    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    public UseAction getUseAction(ItemStack stack) {
        return UseAction.EAT;
    }

    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        // default white text
        String[] effecttext = new String[20];
        effecttext[0] = "nothing";
        effecttext[1] = "poison";
        effecttext[2] = "fire resistance";
        effecttext[3] = "slowness";
        effecttext[4] = "weakness";
        effecttext[5] = "strength";
        effecttext[6] = "water breathing";
        effecttext[7] = "swiftness";
        effecttext[8] = "slow falling";
        effecttext[9] = "jump boost";
        effecttext[10] = "invisibility";
        effecttext[11] = "luck";
        effecttext[12] = "regeneration";
        effecttext[13] = "night vision";
        effecttext[14] = "resistance";

        tooltip.add(Text.literal("cures " + effecttext[itemStack.getOrCreateSubNbt("alchemic").getInt("flavor")]).formatted(Formatting.LIGHT_PURPLE));

    }
}
