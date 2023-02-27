package com.mechanica.item;

import com.mechanica.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab Mechanica_TAB = new CreativeModeTab("minersdream"){
        @Override
        public ItemStack makeIcon() {
                return new ItemStack(ModBlocks.MECHANIC_MINER.get());
        }
    };
}
