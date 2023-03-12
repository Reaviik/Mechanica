package com.mechanica.item;

import com.mechanica.block.ModBlocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;

public class ModTiers {
    public static final ForgeTier MECHANICA = new ForgeTier(4, 2652, 14.0f, 10.0f, 44, BlockTags.NEEDS_DIAMOND_TOOL,
            () -> Ingredient.of(ModBlocks.MECHANIC_MINER_BLOCK.get()));
}
