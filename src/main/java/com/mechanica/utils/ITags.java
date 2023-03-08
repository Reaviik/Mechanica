package com.mechanica.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ITags {
    public static class Blocks {
       // public static final TagKey<Block> ASNIUM_ORE = tag("ores/asnium");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

    public static class Items {
        public static final TagKey<Item> MATRIX = tag("items/matrix");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }
    }

}