package com.mechanica.block.entity;

import com.mechanica.Mechanica;
import com.mechanica.block.ModBlocks;
import com.mechanica.block.entity.custom.miner.MechanicMinerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Mechanica.MOD_ID);

    public static final RegistryObject<BlockEntityType<MechanicMinerBlockEntity>> MECHANIC_MINER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("mechanic_miner_block_entity", () ->
                    BlockEntityType.Builder.of(MechanicMinerBlockEntity::new,
                            ModBlocks.MECHANIC_MINER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
