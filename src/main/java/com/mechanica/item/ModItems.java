package com.mechanica.item;

import com.mechanica.Mechanica;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Mechanica.MOD_ID);


    //BASIC ITEMS
    public static final RegistryObject<Item> STARLUME_MATRIX = ITEMS.register("starlume_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> SUNDUST_MATRIX = ITEMS.register("sundust_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> AQUAMIST_MATRIX = ITEMS.register("aquamist_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EARTHSONG_MATRIX = ITEMS.register("earthsong_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> EMBERGLOW_MATRIX = ITEMS.register("emberglow_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> MOONSHADOW_MATRIX = ITEMS.register("moonshadow_matrix",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SPEED_UPGRADE_LENS = ITEMS.register("speed_upgrade_lens",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.COMMON)));
    public static final RegistryObject<Item> STRENG_UPGRADE_LENS = ITEMS.register("streng_upgrade_lens",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> STARLUME_CRYSTAL= ITEMS.register("starlume_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.RARE)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
