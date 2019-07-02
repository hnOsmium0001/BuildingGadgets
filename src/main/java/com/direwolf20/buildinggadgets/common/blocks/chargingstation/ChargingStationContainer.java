package com.direwolf20.buildinggadgets.common.blocks.chargingstation;

import com.direwolf20.buildinggadgets.common.registry.objects.BGContainers;
import com.direwolf20.buildinggadgets.common.util.exceptions.CapabilityNotPresentException;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class ChargingStationContainer extends Container {
    private ChargingStationTileEntity te;

    public ChargingStationContainer(int windowId, PlayerInventory playerInventory) {
        super(BGContainers.CHARGING_STATION_CONTAINER, windowId);//TODO fix once we get access to ContainerTypes
        //addOwnSlots();
        //addPlayerSlots(playerInventory);
    }

    public ChargingStationContainer(int windowId, PlayerInventory playerInventory, PacketBuffer extraData) {
        super(BGContainers.CHARGING_STATION_CONTAINER, windowId);//TODO fix once we get access to ContainerTypes
        BlockPos pos = extraData.readBlockPos();
        this.te = (ChargingStationTileEntity) Minecraft.getInstance().world.getTileEntity(pos);

        addPlayerSlots(playerInventory);
        addOwnSlots();

        func_216958_a(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergy();
            }

            @Override
            public void set(int value) {
                te.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
            }
        });
    }

    /*public ChargingStationContainer(int windowId, PlayerInventory playerInventory, ChargingStationTileEntity tileEntity) {
        this(windowId, playerInventory);
        this.te = Objects.requireNonNull(tileEntity);
        addPlayerSlots(playerInventory);
        addOwnSlots();
    }*/

    public ChargingStationContainer(int windowId, World world, BlockPos pos, PlayerInventory playerInventory, PlayerEntity player) {
        super(BGContainers.CHARGING_STATION_CONTAINER, windowId);
        this.te = (ChargingStationTileEntity) world.getTileEntity(pos);
        addPlayerSlots(playerInventory);
        addOwnSlots();

        func_216958_a(new IntReferenceHolder() {
            @Override
            public int get() {
                return getEnergy();
            }

            @Override
            public void set(int value) {
                te.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> ((CustomEnergyStorage) h).setEnergy(value));
            }
        });
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return getTe().canInteractWith(playerIn);
    }

    private void addPlayerSlots(PlayerInventory playerInventory) {
        // Slots for the hotbar
        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = 58 + 84;
            addSlot(new Slot(playerInventory, row, x, y));
        }
        // Slots for the main inventory
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + 84;
                addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }


    }

    private void addOwnSlots() {
        IItemHandler itemHandler = this.getTe().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).orElseThrow(CapabilityNotPresentException::new);
        int x = 86;
        int y = 41;

        addSlot(new SlotItemHandler(itemHandler, 0, x, y));
        x = 144;
        addSlot(new SlotItemHandler(itemHandler, 1, x, y));
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(PlayerEntity p_82846_1_, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            //if (!(itemstack1.getItem() instanceof GadgetCopyPaste) && !itemstack1.getItem().equals(Items.PAPER) && !(itemstack1.getItem() instanceof Template))
            //    return itemstack;
            itemstack = itemstack1.copy();

            if (index < ChargingStationTileEntity.SIZE) {
                if (!this.mergeItemStack(itemstack1, ChargingStationTileEntity.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, ChargingStationTileEntity.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    public ChargingStationTileEntity getTe() {
        return te;
    }

    public int getEnergy() {
        return te.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }
}