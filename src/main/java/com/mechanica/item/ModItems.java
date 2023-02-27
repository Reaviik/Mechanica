package com.mechanica.item;

import com.mechanica.Mechanica;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Mechanica.MOD_ID);


    //BASIC ITEMS
    //public static final RegistryObject<Item> ASNIUM_INGOT = ITEMS.register("asnium_ingot",
           // () -> new Item(new Item.Properties().tab(ModCreativeModeTab.MinersDream_TAB).rarity(Rarity.RARE)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
