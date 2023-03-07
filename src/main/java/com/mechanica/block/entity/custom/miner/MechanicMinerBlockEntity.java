package com.mechanica.block.entity.custom.miner;

import com.mechanica.block.ModBlocks;
import com.mechanica.block.entity.ModBlockEntities;
import com.mechanica.block.screen.MechanicMiner.MechanicMinerMenu;
import com.mechanica.block.screen.MechanicMiner.MechanicMinerScreen;
import com.mechanica.item.ModItems;
import com.mechanica.utils.ITags;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import java.util.Random;

public class MechanicMinerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(34) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final LevelAccessor Level = this.getLevel();
    private final BlockPos Pos = this.getBlockPos();
    static int matrix = 0;
    private static int chance = 0;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

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
        //tag.putInt("mechanic_miner.progress", progress);
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
    @Deprecated
    private static boolean hasStructure(MechanicMinerBlockEntity entity) {
        String key = "minecraft:glass";
        BlockState structure = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(key)).defaultBlockState();
        // Direção norte
        boolean northHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int x = entity.getBlockPos().getX() - 1; x <= entity.getBlockPos().getX() + 1; x++) {
                if (entity.getLevel().getBlockState(new BlockPos(x, y, entity.getBlockPos().getZ() - 2)) != structure) {
                    northHasWall = false;
                    break;
                }
            }
        }
        // Direção south
        boolean southHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int x = entity.getBlockPos().getX() - 1; x <= entity.getBlockPos().getX() + 1; x++) {
                if (entity.getLevel().getBlockState(new BlockPos(x, y, entity.getBlockPos().getZ() + 2)) != structure) {
                    southHasWall = false;
                    break;
                }
            }
        }
        // Direção east
        boolean eastHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if (entity.getLevel().getBlockState(new BlockPos(entity.getBlockPos().getX() + 2, y, z)) != structure) {
                    eastHasWall = false;
                    break;
                }
            }
        }
        // Direção west
        boolean westHasWall = true;
        for (int y = entity.getBlockPos().getY() + 1; y >= entity.getBlockPos().getY() - 1; y--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if (entity.getLevel().getBlockState(new BlockPos(entity.getBlockPos().getX() - 2, y, z)) != structure) {
                    westHasWall = false;
                    break;
                }
            }
        }
        // Direção top
        boolean topHasWall = true;
        for (int x = entity.getBlockPos().getX() + 1; x >= entity.getBlockPos().getX() - 1; x--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if (entity.getLevel().getBlockState(new BlockPos(x, entity.getBlockPos().getY() + 2, z)) != structure) {
                    topHasWall = false;
                    break;
                }
            }
        }
        // Direção bottom
        boolean bottomHasWall = true;
        for (int x = entity.getBlockPos().getX() + 1; x >= entity.getBlockPos().getX() - 1; x--) {
            for (int z = entity.getBlockPos().getZ() - 1; z <= entity.getBlockPos().getZ() + 1; z++) {
                if (entity.getLevel().getBlockState(new BlockPos(x, entity.getBlockPos().getY() - 2, z)) != structure) {
                    if (!new BlockPos(x, entity.getBlockPos().getY() - 2, z).equals(new BlockPos(entity.getBlockPos().getX(), entity.getBlockPos().getY() - 2, entity.getBlockPos().getZ()))) {
                        bottomHasWall = false;
                        break;
                    }
                }
            }
        }
        return northHasWall && eastHasWall && southHasWall && eastHasWall && westHasWall && topHasWall && bottomHasWall;
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

        for (int i = 0; i < firstSix.length; i++) {
            for (int j = 0; j < lastSix.length; j++) {
                if (firstSix[i] == lastSix[j]) {
                    stability = stability + (j + 1);
                    MechanicMinerScreen.stability = (int) (stability * 4.166);
                    LOGGER.info(String.valueOf(stability));
                }
            }
        }
        return stability != 0;
    }

    public static boolean hasFull(LevelAccessor level, BlockPos pPos, MechanicMinerBlockEntity entity){
        return hasStabilizer(level,pPos) && hasStructure(entity);
    }
    public static boolean isMatrix(MechanicMinerBlockEntity entity) {
        ItemStack item = entity.itemHandler.getStackInSlot(0);
        return item.is(ITags.Items.MATRIX);
    }
    public static double hasSpeedUP(MechanicMinerBlockEntity entity) {
        double speed = 0;
        double streng = 0;
        for (int i = 1; i <= 4; i++) {
            if (entity.itemHandler.getStackInSlot(i).getItem() == Items.COBBLESTONE.getDefaultInstance().getItem()) {
                speed += 0.25;
            }
        }
        return speed;
    }
    public static double hasStrengUP(MechanicMinerBlockEntity entity) {
        double streng = 0;
        for (int i = 1; i <= 4; i++) {
            //TODO
            if (entity.itemHandler.getStackInSlot(i).getItem() == Items.STONE.getDefaultInstance().getItem()) {
                streng += 1;
            }
        }
        return streng;
    }
    //Imput Manager
    public static void getChance(MechanicMinerBlockEntity entity) {
        chance = 0;
        ItemStack matrix = new ItemStack(entity.itemHandler.getStackInSlot(0).getItem());
                if (matrix.getItem() == ModItems.STARLUME_MATRIX.get()) {
                    chance += 4;
                }
                if (matrix.getItem() == ModItems.SUNDUST_MATRIX.get()) {
                    chance += 8;
                }
                if (matrix.getItem() == ModItems.AQUAMIST_MATRIX.get()) {
                    chance += 16;
                }
                if (matrix.getItem() == ModItems.EARTHSONG_MATRIX.get()) {
                    chance += 32;
                }
                if (matrix.getItem() == ModItems.EMBERGLOW_MATRIX.get()) {
                    chance += 64;
                }
                if (matrix.getItem() == ModItems.MOONSHADOW_MATRIX.get()) {
                    chance += 128;
                }

                MechanicMinerScreen.status = new String(String.valueOf((chance) * (hasSpeedUP(entity) + hasStrengUP(entity))));
            }
    public static void craft(LevelAccessor level, BlockPos pPos, MechanicMinerBlockEntity entity){
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
            if (drop != null){
                ItemStack finalDrop = drop;
                BlockEntity inventory = level.getBlockEntity(new BlockPos(pPos));
                drop.setCount(5);
                inventory.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(capability -> {
                    if (capability instanceof IItemHandlerModifiable) {
                        for(int i = 7; i < capability.getSlots(); i++){
                            LOGGER.info("Loop");
                            if(capability.getStackInSlot(i).isEmpty() || capability.getStackInSlot(i).getCount() < capability.getStackInSlot(i).getMaxStackSize() && capability.getStackInSlot(i) == finalDrop) {
                                if(matrix <= 0){
                                    matrix = 5;
                                    capability.extractItem(0, 1, true);
                                 }else{
                                    matrix--;
                                }
                                capability.insertItem(i,finalDrop,true);
                                LOGGER.info("Inserir");
                                break;
                            }
                        }
                    }
                });
            }
        }
    }
    static int tick = 0;
    //Tick Manager
    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, MechanicMinerBlockEntity pBlockEntity) {
        if (pBlockEntity.progress == pBlockEntity.maxProgress) {
            setChanged(pLevel, pPos, pState);
            craft(pLevel,pPos,pBlockEntity);
        }
        if (pBlockEntity.progress <= pBlockEntity.maxProgress) {
            setChanged(pLevel, pPos, pState);
            pBlockEntity.progress++;
        }
        else {
            setChanged(pLevel, pPos, pState);
            getChance(pBlockEntity);
            hasStabilizer(pLevel, pPos);
            hasStructure(pBlockEntity);
            tick++;
            pBlockEntity.resetProgress();
        }
    }
    //Reseta o progresso
    private void resetProgress() {
        this.progress = 0;
    }

}
