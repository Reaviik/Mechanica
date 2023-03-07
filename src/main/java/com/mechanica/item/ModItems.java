package com.mechanica.item;

import com.mechanica.Mechanica;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

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
    public static final RegistryObject<Item> STARLUME_CRYSTAL= ITEMS.register("starlume_crystal",
            () -> new Item(new Item.Properties().tab(ModCreativeModeTab.Mechanica_TAB).rarity(Rarity.RARE)));

}
