package com.mechanica.item;

import com.mechanica.block.ModBlocks;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab Mechanica_TAB = new CreativeModeTab("Mechanica"){
        @Override
        public ItemStack makeIcon() {
                return new ItemStack(ModItems.AQUAMIST_MATRIX.get());
        }
    };
}
