package com.mechanica.block.screen.slot;


import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModUpgradeSlot extends SlotItemHandler {
    public ModUpgradeSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    //TODO
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return (Blocks.INFESTED_COBBLESTONE.asItem() == stack.getItem());
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
