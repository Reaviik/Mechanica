package com.mechanica.block.screen.slot;


import com.mechanica.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModMinerStrengSlot extends SlotItemHandler {
    public ModMinerStrengSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    //TODO
    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return ModItems.STRENG_UPGRADE_LENS.get().asItem() == stack.getItem() && stack.getCount() == 1;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
