package com.mechanica.block.entity.custom.miner;

import com.mechanica.block.ModBlocks;
import com.mechanica.block.entity.ModBlockEntities;
import com.mechanica.block.screen.MechanicMiner.MechanicaMinerMenu;
import com.mechanica.block.screen.MechanicMiner.MechanicaMinerScreen;
import com.mechanica.config.MechanicaCommonConfigs;
import com.mechanica.item.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Random;


public class MechanicaMinerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(34) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<ItemStackHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 180;
    private int quantity = 1;
    private static int matrix = 0;
    public MechanicaMinerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.MECHANICA_MINER_BLOCK_ENTITY.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> MechanicaMinerBlockEntity.this.progress;
                    case 1 -> MechanicaMinerBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> MechanicaMinerBlockEntity.this.progress = pValue;
                    case 1 -> MechanicaMinerBlockEntity.this.maxProgress = pValue;
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
        return new MechanicaMinerMenu(pContainerId, pPlayerInventory, this);
    }
    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @javax.annotation.Nullable Direction side) {
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
    public void hasSpeedUP(MechanicaMinerBlockEntity entity) {
        int speed = 180;
        for (int i = 1; i <= 4; i++) {
            if (entity.itemHandler.getStackInSlot(i).getItem().getDefaultInstance().getItem() == Items.COBBLESTONE.getDefaultInstance().getItem()) {
                speed -= 20;
            }
        }
        progress = speed;
    }
    public void hasStrengUP(MechanicaMinerBlockEntity entity) {
        int streng = 1;
        for (int i = 1; i <= 4; i++) {
            //TODO
            if (entity.itemHandler.getStackInSlot(i).getItem() == Items.STONE.getDefaultInstance().getItem()) {
                streng += 2;
            }
        }
        quantity = streng;
    }
    @Deprecated
    private static boolean hasStructure(MechanicaMinerBlockEntity entity){
        String key = "minecraft:glass";

        BlockState structure = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key)).defaultBlockState();
        // Direção norte
        boolean northHasWall= true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int x = entity.getBlockPos().getX() - 1; x <= entity.getBlockPos().getX() + 1; x++) {
                if(entity.getLevel().getBlockState(new BlockPos(x,y,entity.getBlockPos().getZ() - 2)) != structure){
                    northHasWall = false;
                    break;
                }
            }
        }
        // Direção south
        boolean southHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int x = entity.getBlockPos().getX() - 1; x <= entity.getBlockPos().getX() + 1; x++) {
                if(entity.getLevel().getBlockState(new BlockPos(x,y,entity.getBlockPos().getZ() + 2)) != structure){
                    southHasWall = false;
                    break;
                }
            }
        }
        // Direção east
        boolean eastHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if(entity.getLevel().getBlockState(new BlockPos(entity.getBlockPos().getX() + 2,y,z)) != structure){
                    eastHasWall = false;
                    break;
                }
            }
        }
        // Direção west
        boolean westHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if(entity.getLevel().getBlockState(new BlockPos(entity.getBlockPos().getX() - 2,y,z)) != structure){
                    westHasWall = false;
                    break;
                }
            }
        }
        // Direção top
        boolean topHasWall = true;
        for (int x = entity.getBlockPos().getX() + 1; x >= entity.getBlockPos().getX() - 1; x--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if(entity.getLevel().getBlockState(new BlockPos(x, entity.getBlockPos().getY() + 2, z)) != structure){
                    topHasWall = false;
                    break;
                }
            }
        }
        // Direção bottom
        boolean bottomHasWall = true;
        for (int x = entity.getBlockPos().getX() + 1; x >= entity.getBlockPos().getX() - 1; x--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if(entity.getLevel().getBlockState(new BlockPos(x, entity.getBlockPos().getY() - 2, z)) != structure){
                    if(!new BlockPos(x, entity.getBlockPos().getY() - 2, z).equals(new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() - 2, entity.getBlockPos().getZ()))) {
                        bottomHasWall = false;
                        break;
                    }
                }
            }
        }
        return northHasWall && eastHasWall && southHasWall && westHasWall && topHasWall && bottomHasWall;
    }
    public static boolean hasStabilizer(LevelAccessor pLevel, BlockPos pPos) {
        int stability = 0;
        BlockState x02 = pLevel.getBlockState(new BlockPos(pPos.getX() - 3, pPos.getY(), pPos.getZ())).getBlock().defaultBlockState();
        BlockState x20 = pLevel.getBlockState(new BlockPos(pPos.getX() + 3, pPos.getY(), pPos.getZ())).getBlock().defaultBlockState();
        BlockState z02 = pLevel.getBlockState(new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() - 3)).getBlock().defaultBlockState();
        BlockState z20 = pLevel.getBlockState(new BlockPos(pPos.getX(), pPos.getY(), pPos.getZ() + 3)).getBlock().defaultBlockState();

        BlockState t1 = ModBlocks.STABILIZER.get().defaultBlockState();
        BlockState t2 = Blocks.AMETHYST_BLOCK.defaultBlockState();
        BlockState t3 = Blocks.IRON_ORE.defaultBlockState();
        BlockState t4 = Blocks.ACACIA_DOOR.defaultBlockState();
        BlockState t5 = Blocks.ACACIA_LOG.defaultBlockState();
        BlockState t6 = Blocks.CHEST.defaultBlockState();

        BlockState[] firstSix = new BlockState[]{x02, x20, z02, z20};
        BlockState[] lastSix = new BlockState[]{t1, t2, t3, t4, t5, t6};

        for (BlockState six : firstSix) {
            for (int j = 0; j < lastSix.length; j++) {
                if (six == lastSix[j]) {
                    stability = stability + (j + 1);
                    MechanicaMinerScreen.stability = (int) (stability * 4.166);
                    LOGGER.info(String.valueOf(stability));
                }
            }
        }
        return stability != 0;
    }
    public static boolean hasFull(LevelAccessor level, BlockPos pPos, MechanicaMinerBlockEntity entity){
        return hasStabilizer(level,pPos) && hasStructure(entity);
    }
    public static boolean isMatrix(MechanicaMinerBlockEntity entity) {
        Item item = entity.itemHandler.getStackInSlot(0).getItem();
        //return item.is(ITags.Items.MATRIX);
        return item == ModItems.AQUAMIST_MATRIX.get() ||
                item == ModItems.EARTHSONG_MATRIX.get() ||
                item == ModItems.MOONSHADOW_MATRIX.get() ||
                item == ModItems.EMBERGLOW_MATRIX.get() ||
                item == ModItems.SUNDUST_MATRIX.get() ||
                item == ModItems.STARLUME_CRYSTAL.get();
    }
    public static void craft(LevelAccessor level, BlockPos pPos, MechanicaMinerBlockEntity entity){
        if(hasFull(level,pPos,entity) && isMatrix(entity)){
            ItemStack drop = null;

            ItemStack[] cinquenta = {Blocks.IRON_ORE.asItem().getDefaultInstance(), Blocks.DEEPSLATE_IRON_ORE.asItem().getDefaultInstance(),
                    Blocks.COAL_ORE.asItem().getDefaultInstance(), Blocks.COPPER_ORE.asItem().getDefaultInstance()};
            ItemStack[] trinta = {Blocks.REDSTONE_ORE.asItem().getDefaultInstance(), Blocks.LAPIS_ORE.asItem().getDefaultInstance(),
                    Blocks.CALCITE.asItem().getDefaultInstance(), Blocks.AMETHYST_BLOCK.asItem().getDefaultInstance()};
            ItemStack[] quinze = {Blocks.GOLD_ORE.asItem().getDefaultInstance(), Blocks.ANDESITE.asItem().getDefaultInstance(),
                    Blocks.DIORITE.asItem().getDefaultInstance(), Blocks.GRANITE.asItem().getDefaultInstance()};
            ItemStack[] cinco = {Blocks.DIAMOND_ORE.asItem().getDefaultInstance(), Blocks.DEEPSLATE.asItem().getDefaultInstance(),
                    Blocks.DEEPSLATE_DIAMOND_ORE.asItem().getDefaultInstance(), Blocks.DEEPSLATE_GOLD_ORE.asItem().getDefaultInstance()};
            ItemStack[] zero = {Blocks.EMERALD_ORE.asItem().getDefaultInstance(), Blocks.GOLD_BLOCK.asItem().getDefaultInstance(),
                    Blocks.IRON_ORE.asItem().getDefaultInstance(), Blocks.COPPER_BLOCK.asItem().getDefaultInstance()};

            Random random = new Random();
            int randomNumber = random.nextInt(99);
            if(randomNumber == 0){
                int randomzero = random.nextInt(zero.length);
                drop = zero[randomzero];
            }
            //5
            if(randomNumber > 0 && randomNumber <= 5){
                int randomCinco = random.nextInt(cinco.length);
                drop = cinco[randomCinco];
            }
            //10
            if(randomNumber > 5 && randomNumber <= 20){
                int randomQuinze = random.nextInt(quinze.length);
                drop = quinze[randomQuinze];
            }
            //30
            if(randomNumber > 20 && randomNumber <=50){
                int randomTrinta = random.nextInt(trinta.length);
                drop = trinta[randomTrinta];
            }
            //50
            if(randomNumber > 50){
                int randomCinquenta = random.nextInt(cinquenta.length);
                drop = cinquenta[randomCinquenta];
            }
            ItemStack finalDrop = drop;
            BlockEntity inventory = level.getBlockEntity(pPos);
            ItemStack netherite = Blocks.ANCIENT_DEBRIS.asItem().getDefaultInstance();
            netherite.setCount(MechanicaCommonConfigs.NETHERITE_DROP_CHANCE.get());
            finalDrop.setCount(1);
            inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                if(capability instanceof IItemHandlerModifiable) {
                    for (int i = 7; i < capability.getSlots(); i++) {
                        ItemStack item = capability.getStackInSlot(i);
                        if (item.isEmpty()) {
                            for (int j = 0; j < entity.quantity; j++){
                                capability.insertItem(i, finalDrop, false);
                                capability.insertItem(32, netherite, false);
                                matrix--;
                                if (matrix <= 0){
                                    capability.extractItem(0,1,true);
                                    matrix = 5;
                                }
                            }
                            break;
                        } else if (item.getItem() == finalDrop.getItem() && item.getCount() < capability.getSlotLimit(i)) {
                            for (int l = 0; l < entity.quantity; l++){
                                capability.insertItem(i, finalDrop, false);
                                capability.insertItem(32, netherite, false);
                                LOGGER.info(""+netherite);
                                LOGGER.info(""+finalDrop);
                                matrix--;
                                if (matrix <= 0){
                                    capability.extractItem(0,1,true);
                                    matrix = 5;
                                }
                            }
                            break;
                        }
                    }
                }
            });
        }
    }
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, MechanicaMinerBlockEntity pBlockEntity) {
        if (pBlockEntity.progress < pBlockEntity.maxProgress) {
            setChanged(pLevel, pPos, pState);
            pBlockEntity.progress++;
        }
        else {
            pBlockEntity.hasStrengUP(pBlockEntity);
            pBlockEntity.hasSpeedUP(pBlockEntity);
            setChanged(pLevel, pPos, pState);
            craft(pLevel,pPos,pBlockEntity);
            pBlockEntity.resetProgress();
        }
    }
    private void resetProgress() {
        this.progress = 0;
    }

}