package com.mechanica.block.screen.slot;


import com.mechanica.block.ModBlocks;
import com.mechanica.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModMinerSpeedSlot extends SlotItemHandler {
    public ModMinerSpeedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    //TODO
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return ModItems.SPEED_UPGRADE_LENS.get().asItem() == stack.getItem() && stack.getCount() == 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
