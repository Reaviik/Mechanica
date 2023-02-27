package com.mechanica.block.entity.custom.mechanic;

import com.mechanica.block.entity.ModBlockEntities;
import com.mechanica.block.screen.MechanicMiner.MechanicMinerMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.Nonnull;

import static net.minecraft.sounds.SoundSource.BLOCKS;

public class MechanicMinerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(34) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 180;
    public MechanicMinerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MECHANIC_MINER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                switch (pIndex) {
                    case 0:
                        return MechanicMinerBlockEntity.this.progress;
                    case 1:
                        return MechanicMinerBlockEntity.this.maxProgress;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0:
                        MechanicMinerBlockEntity.this.progress = pValue;
                        break;
                    case 1:
                        MechanicMinerBlockEntity.this.maxProgress = pValue;
                        break;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Mechanic Miner");
    }
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new MechanicMinerMenu(pContainerId, pPlayerInventory, this, this.data);
    }
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        tag.put("inventory", itemHandler.serializeNBT());
        tag.putInt("mechanic_miner.progress", progress);
        super.saveAdditional(tag);
    }
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }
    private static final Logger LOGGER = LogUtils.getLogger();
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }
    //TODO
    private static boolean hasRecipe(MechanicMinerBlockEntity entity) {
        return false;
    }

    private static int hasUpgrades(@NotNull MechanicMinerBlockEntity entity) {
        //Reseta o MaxProgress do bloco
        int overclock = 0;
        entity.maxProgress = 180;
        //Verifica todos os slots de 1 a 3
        for (int i = 1; i <= 3; i++) {
            //TODO
            if (entity.itemHandler.getStackInSlot(i).getItem() == Items.COAL) {
                entity.maxProgress -= entity.maxProgress * 0.5;
                overclock++;
            }
        }
        return overclock;
    }
    private static void craftItem(MechanicMinerBlockEntity entity) {
        hasUpgrades(entity);
        Level level = entity.level;
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }
            entity.resetProgress();
    }
    //Tick Manager
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, MechanicMinerBlockEntity pBlockEntity) {
            if(pBlockEntity.progress == pBlockEntity.maxProgress && hasRecipe(pBlockEntity)) {
                setChanged(pLevel, pPos, pState);
                craftItem(pBlockEntity);
                }
            if(pBlockEntity.progress <= pBlockEntity.maxProgress){
                setChanged(pLevel, pPos, pState);
                pBlockEntity.progress++;
            }else{
                setChanged(pLevel, pPos, pState);
                pBlockEntity.resetProgress();
            }
    }
    //Reseta o progresso
    private void resetProgress() {
        this.progress = 0;
    }

}
