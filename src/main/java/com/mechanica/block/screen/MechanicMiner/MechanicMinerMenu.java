package com.mechanica.block.screen.MechanicMiner;

import com.mechanica.block.ModBlocks;
import com.mechanica.block.entity.custom.miner.MechanicMinerBlockEntity;
import com.mechanica.block.screen.ModMenuTypes;
import com.mechanica.block.screen.slot.ModInputSlot;
import com.mechanica.block.screen.slot.ModResultSlot;
import com.mechanica.block.screen.slot.ModUpgradeSlot;
import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import org.slf4j.Logger;

public class MechanicMinerMenu extends AbstractContainerMenu {
    private final MechanicMinerBlockEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public MechanicMinerMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level.getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(2));
    }
    private static final Logger LOGGER = LogUtils.getLogger();
    //Responsavel por setar a posição dos slots
    public MechanicMinerMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        super(ModMenuTypes.MECHANIC_MINER_MENU.get(), pContainerId);
        checkContainerSize(inv, 34);
        blockEntity = ((MechanicMinerBlockEntity) entity);
        this.level = inv.player.level;
        this.data = data;
        //Não sei
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
            //Slot de entrada
            this.addSlot(new ModInputSlot(handler, 0, 19, -31));
            //Slots 1,2,3 slots de upgrade
            //       1
            // 0   2 3 4
            //       5 6
            this.addSlot(new ModUpgradeSlot(handler, 1, 115, -49));
            this.addSlot(new ModUpgradeSlot(handler, 2, 97, -31));
            this.addSlot(new ModUpgradeSlot(handler, 3, 115, -31));
            this.addSlot(new ModUpgradeSlot(handler, 4, 133, -31));
            this.addSlot(new ModUpgradeSlot(handler, 5, 115, -13));
            this.addSlot(new ModUpgradeSlot(handler, 6, 133, -13));
            //Primeira fileira
            this.addSlot(new ModResultSlot(handler, 7, 8, 14));
            this.addSlot(new ModResultSlot(handler, 8, 26, 14));
            this.addSlot(new ModResultSlot(handler, 9, 44, 14));
            this.addSlot(new ModResultSlot(handler, 10, 62, 14));
            this.addSlot(new ModResultSlot(handler, 11, 80, 14));
            this.addSlot(new ModResultSlot(handler, 12, 98, 14));
            this.addSlot(new ModResultSlot(handler, 13, 116, 14));
            this.addSlot(new ModResultSlot(handler, 14, 134, 14));
            this.addSlot(new ModResultSlot(handler, 15, 152, 14));
            //Primeira fileira
            this.addSlot(new ModResultSlot(handler, 16, 8, 32));
            this.addSlot(new ModResultSlot(handler, 17, 26, 32));
            this.addSlot(new ModResultSlot(handler, 18, 44, 32));
            this.addSlot(new ModResultSlot(handler, 19, 62, 32));
            this.addSlot(new ModResultSlot(handler, 20, 80, 32));
            this.addSlot(new ModResultSlot(handler, 21, 98, 32));
            this.addSlot(new ModResultSlot(handler, 22, 116, 32));
            this.addSlot(new ModResultSlot(handler, 23, 134, 32));
            this.addSlot(new ModResultSlot(handler, 24, 152, 32));
            //Primeira fileira
            this.addSlot(new ModResultSlot(handler, 25, 8, 50));
            this.addSlot(new ModResultSlot(handler, 26, 26, 50));
            this.addSlot(new ModResultSlot(handler, 27, 44, 50));
            this.addSlot(new ModResultSlot(handler, 28, 62, 50));
            this.addSlot(new ModResultSlot(handler, 29, 80, 50));
            this.addSlot(new ModResultSlot(handler, 30, 98, 50));
            this.addSlot(new ModResultSlot(handler, 31, 116, 50));
            this.addSlot(new ModResultSlot(handler, 32, 134, 50));
            this.addSlot(new ModResultSlot(handler, 33, 152, 50));

        });
    }

    // CRÉDITO VAI PARA: diesieben07 | https://github.com/diesieben07/SevenCommons
    // deve atribuir um número de slot a cada um dos slots usados pela GUI.
    // Para este contêiner, podemos ver tanto os slots do inventário de blocos quanto os slots do inventário do jogador e a barra de acesso.
    // Cada vez que adicionamos um Slot ao container, ele aumenta automaticamente o slotIndex, o que significa
    // 0 - 8 = slots de hotbar (que serão mapeados para os números de slot do InventoryPlayer 0 - 8)
    // 9 - 35 = espaços de inventário do jogador (que mapeiam para os números de espaço do InventoryPlayer 9 - 35)
    // 36 - 44 = slots TileInventory, que mapeiam para nossos números de slot TileEntity 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
    //DEFINA ISSO PELO AMOR DE DEUS!
    private static final int TE_INVENTORY_SLOT_COUNT = 34;  // Numero de slots que o bloco tem!!!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Verifique se o slot clicado é um dos slots do contêiner
        //Não sei '-'
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            //Não sei '-'
            // Este é um slot de contêiner vanilla, então mescle a pilha no inventário de blocos
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            //Não sei '-'
            // Este é um slot TE, então mescle a pilha no inventário do jogador
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        //Praque isso serve?
        // Se o tamanho da pilha == 0 (toda a pilha foi movida), defina o conteúdo do slot como nulo
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            //I dont undestand Weee
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }
    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.MECHANIC_MINER.get());
    }
    //Faz 0 sentido pra min, porque ta adicionando slot ao inventario do player? e o meu inventario? só o player ganha slot extra?
    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot((new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 85 + i * 18)));
            }
        }
    }
    //Desentendo completamente, mas denove, bah meu o player, adiciona no meu tambem
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 143));
        }
    }


}
