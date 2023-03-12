package com.mechanica.block;


import com.mechanica.Mechanica;
import com.mechanica.block.custom.MechanicMiner.MechanicaMinerBlock;
import com.mechanica.block.custom.Stabilizers.Stabilizer;
import com.mechanica.item.ModCreativeModeTab;
import com.mechanica.item.ModItems;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, Mechanica.MOD_ID);

    public static final RegistryObject<Block> MECHANIC_MINER_BLOCK = registerBlock("mechanica_miner_block",
            () -> new MechanicaMinerBlock(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()
                    .strength(9f).lightLevel(LUZ -> {return 1;})), ModCreativeModeTab.Mechanica_TAB,"mechanica_miner");
    public static final RegistryObject<Block> STABILIZER = registerBlock("stabilizer",
            () -> new Stabilizer(BlockBehaviour.Properties.of(Material.METAL).noOcclusion()
                    .strength(3f).lightLevel(LUZ -> {return 1;})), ModCreativeModeTab.Mechanica_TAB,"stabilizer");


    //Tooltip
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blocks, CreativeModeTab tab, String tooltipKey){
        RegistryObject<T> toReturn = BLOCKS.register(name, blocks);
        registerBlockItem(name, toReturn, tab, tooltipKey);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab, String tooltipKey){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),  new Item.Properties().tab(tab)){
            @Override
            public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
                pTooltip.add(new TranslatableComponent("tooltip.mechanica.pressShift"));
                if(Screen.hasShiftDown()) {
                    pTooltip.remove(1);
                    pTooltip.add(new TranslatableComponent(tooltipKey));
                }
            }
        });
    }
    //No Tooltip
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> blocks, CreativeModeTab tab){
        RegistryObject<T> toReturn = BLOCKS.register(name, blocks);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block, CreativeModeTab tab){
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),  new Item.Properties().tab(tab)));
    }
    public static void register(IEventBus eventBus){
        BLOCKS.register(eventBus);
    }
}