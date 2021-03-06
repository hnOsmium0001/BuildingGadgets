package com.direwolf20.buildinggadgets.common.tools;

import com.direwolf20.buildinggadgets.common.items.ModItems;
import com.direwolf20.buildinggadgets.common.items.gadgets.GadgetCopyPaste;
import com.direwolf20.buildinggadgets.common.items.gadgets.GadgetGeneric;
import com.direwolf20.buildinggadgets.common.items.pastes.ConstructionPaste;
import com.direwolf20.buildinggadgets.common.items.pastes.GenericPasteContainer;
import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.block.*;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.*;

public class InventoryManipulation {
    private static IProperty AXIS = PropertyEnum.create("axis", EnumFacing.Axis.class);
    private static final Set<IProperty> SAFE_PROPERTIES = ImmutableSet.of(BlockSlab.HALF, BlockStairs.HALF, BlockLog.LOG_AXIS, AXIS, BlockDirectional.FACING,
            BlockStairs.FACING, BlockTrapDoor.HALF, BlockTorch.FACING, BlockStairs.SHAPE, BlockLever.FACING, BlockLever.POWERED, BlockRedstoneRepeater.DELAY,
            BlockStoneSlab.VARIANT, BlockWoodSlab.VARIANT, BlockDoubleWoodSlab.VARIANT, BlockDoubleStoneSlab.VARIANT);

    private static final Set<IProperty> SAFE_PROPERTIES_COPY_PASTE = ImmutableSet.<IProperty>builder().addAll(SAFE_PROPERTIES)
            .addAll(ImmutableSet.of(BlockDoubleWoodSlab.VARIANT, BlockRail.SHAPE, BlockRailPowered.SHAPE)).build();

    public static boolean giveItem(ItemStack itemStack, EntityPlayer player, World world) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }
        if (itemStack.getItem() instanceof ConstructionPaste) {
            itemStack = addPasteToContainer(player, itemStack);
        }
        if (itemStack.getCount() == 0) {
            return true;
        }

        //Try to insert into the remote inventory.
        ItemStack tool = GadgetGeneric.getGadget(player);
        IItemHandler remoteInventory = GadgetUtils.getRemoteInventory(tool, world, player, NetworkIO.Operation.INSERT);
        if (remoteInventory != null) {
            for (int i = 0; i < remoteInventory.getSlots(); i++) {
                ItemStack containerItem = remoteInventory.getStackInSlot(i);
                ItemStack giveItemStack = itemStack.copy();
                if ((containerItem.getItem() == itemStack.getItem() && containerItem.getMetadata() == itemStack.getMetadata()) || containerItem.isEmpty()) {
                    giveItemStack = remoteInventory.insertItem(i, giveItemStack, false);
                    if (giveItemStack.isEmpty())
                        return true;

                    itemStack = giveItemStack.copy();
                }
            }
        }

        //Fill any unfilled stacks in the player's inventory first
        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return false;

        List<Integer> slots = findItem(itemStack.getItem(), itemStack.getMetadata(), currentInv);
        for (int slot : slots) {
            ItemStack stackInSlot = currentInv.getStackInSlot(slot);
            if (stackInSlot.getCount() < stackInSlot.getItem().getItemStackLimit(stackInSlot)) {
                ItemStack giveItemStack = itemStack.copy();
                boolean success = player.inventory.addItemStackToInventory(giveItemStack);
                if (success) return true;
            }
        }

        List<IItemHandler> invContainers = findInvContainers(player);
        if (invContainers.size() > 0) {
            for (IItemHandler container : invContainers) {
                for (int i = 0; i < container.getSlots(); i++) {
                    ItemStack containerItem = container.getStackInSlot(i);
                    ItemStack giveItemStack = itemStack.copy();
                    if (containerItem.getItem() == giveItemStack.getItem() && containerItem.getMetadata() == giveItemStack.getMetadata()) {
                        giveItemStack = container.insertItem(i, giveItemStack, false);
                        if (giveItemStack.isEmpty())
                            return true;

                        itemStack = giveItemStack.copy();
                    }
                }
            }
        }
        ItemStack giveItemStack = itemStack.copy();
        return player.inventory.addItemStackToInventory(giveItemStack);
    }

    /**
     * Call {@link GadgetUtils#clearCachedRemoteInventory GadgetUtils#clearCachedRemoteInventory} when done using this method
     */
    public static boolean useItem(ItemStack itemStack, EntityPlayer player, int count, World world) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        ItemStack tool = GadgetGeneric.getGadget(player);
        IItemHandler remoteInventory = GadgetUtils.getRemoteInventory(tool, world, player);
        if (remoteInventory != null) {
            for (int i = 0; i < remoteInventory.getSlots(); i++) {
                ItemStack containerItem = remoteInventory.getStackInSlot(i);
                if (containerItem.getItem() == itemStack.getItem() && containerItem.getMetadata() == itemStack.getMetadata() && containerItem.getCount() >= count) {
                    remoteInventory.extractItem(i, count, false);
                    return true;
                }
            }
        }


        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return false;

        List<Integer> slots = findItem(itemStack.getItem(), itemStack.getMetadata(), currentInv);
        List<IItemHandler> invContainers = findInvContainers(player);

        if (invContainers.size() > 0) {
            for (IItemHandler container : invContainers) {
                for (int i = 0; i < container.getSlots(); i++) {
                    ItemStack containerItem = container.getStackInSlot(i);
                    if (containerItem.getItem() == itemStack.getItem() && containerItem.getMetadata() == itemStack.getMetadata() && containerItem.getCount() >= count) {
                        container.extractItem(i, count, false);
                        return true;
                    }
                }
            }
        }
        if (slots.size() == 0) {
            return false;
        }
        int slot = slots.get(0);
        ItemStack stackInSlot = currentInv.getStackInSlot(slot);
        if (stackInSlot.getCount() < count) {
            return false;
        }
        stackInSlot.shrink(count);
        return true;
    }

    public interface IRemoteInventoryProvider {
        int countItem(ItemStack tool, ItemStack stack);
    }

    /**
     * Call {@link GadgetUtils#clearCachedRemoteInventory GadgetUtils#clearCachedRemoteInventory} when done using this method
     */
    public static int countItem(ItemStack itemStack, EntityPlayer player, World world) {
        return countItem(itemStack, player, (tool, stack) -> {
            IItemHandler remoteInventory = GadgetUtils.getRemoteInventory(tool, world, player);
            return remoteInventory == null ? 0 : countInContainer(remoteInventory, stack.getItem(), stack.getMetadata());
        });
    }

    public static int countItem(ItemStack itemStack, EntityPlayer player, IRemoteInventoryProvider remoteInventory) {
        if (player.capabilities.isCreativeMode)
            return Integer.MAX_VALUE;

        long count = remoteInventory.countItem(GadgetGeneric.getGadget(player), itemStack);

        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return 0;

        List<Integer> slots = findItem(itemStack.getItem(), itemStack.getMetadata(), currentInv);
        List<IItemHandler> invContainers = findInvContainers(player);
        if (slots.size() == 0 && invContainers.size() == 0 && count == 0) {
            return 0;
        }

        if (invContainers.size() > 0) {
            for (IItemHandler container : invContainers) {
                count += countInContainer(container, itemStack.getItem(), itemStack.getMetadata());
            }
        }

        for (int slot : slots) {
            ItemStack stackInSlot = currentInv.getStackInSlot(slot);
            count += stackInSlot.getCount();
        }
        return longToInt(count);
    }

    public static IntList countItems(List<ItemStack> items, EntityPlayer player) {
        IntList result = new IntArrayList();
        for (ItemStack item : items) {
            result.add(countItem(item, player, player.world));
        }
        return result;
    }

    public static int countPaste(EntityPlayer player) {
        if (player.capabilities.isCreativeMode) {
            return Integer.MAX_VALUE;
        }

        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return 0;

        long count = 0;
        Item item = ModItems.constructionPaste;
        List<Integer> slots = findItem(item, 0, currentInv);
        if (slots.size() > 0) {
            for (int slot : slots) {
                ItemStack stackInSlot = currentInv.getStackInSlot(slot);
                count += stackInSlot.getCount();
            }
        }
        List<Integer> containerSlots = findItemClass(GenericPasteContainer.class, currentInv);
        if (containerSlots.size() > 0) {
            for (int slot : containerSlots) {
                ItemStack stackInSlot = currentInv.getStackInSlot(slot);
                if (stackInSlot.getItem() instanceof GenericPasteContainer) {
                    count += GenericPasteContainer.getPasteAmount(stackInSlot);
                }
            }
        }
        return longToInt(count);
    }

    public static int longToInt(long count)
    {
        try {
            return Math.toIntExact(count);
        } catch (ArithmeticException e) {
            return Integer.MAX_VALUE;
        }
    }

    public static ItemStack addPasteToContainer(EntityPlayer player, ItemStack itemStack) {
        if (!(itemStack.getItem() instanceof ConstructionPaste))
            return itemStack;

        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return itemStack;

        List<Integer> slots = findItemClass(GenericPasteContainer.class, currentInv);
        if (slots.size() == 0)
            return itemStack;

        Map<Integer, Integer> slotMap = new HashMap<>();
        for (int slot : slots) {
            slotMap.put(slot, GenericPasteContainer.getPasteAmount(currentInv.getStackInSlot(slot)));
        }
        List<Map.Entry<Integer, Integer>> list = new ArrayList<>(slotMap.entrySet());
        Comparator<Map.Entry<Integer, Integer>> comparator = Comparator.comparing(Map.Entry::getValue);
        comparator = comparator.reversed();
        list.sort(comparator);


        for (Map.Entry<Integer, Integer> entry : list) {
            ItemStack containerStack = currentInv.getStackInSlot(entry.getKey());
            int maxAmount = ((GenericPasteContainer) containerStack.getItem()).getMaxCapacity();
            int pasteInContainer = GenericPasteContainer.getPasteAmount(containerStack);
            int freeSpace = maxAmount - pasteInContainer;
            int stackSize = itemStack.getCount();
            int remainingPaste = stackSize - freeSpace;
            if (remainingPaste < 0) {
                remainingPaste = 0;
            }
            int usedPaste = Math.abs(stackSize - remainingPaste);
            itemStack.setCount(remainingPaste);
            GenericPasteContainer.setPasteAmount(containerStack, pasteInContainer + usedPaste);
        }
        return itemStack;
    }

    public static boolean usePaste(EntityPlayer player, int count) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return false;

        List<Integer> slots = findItem(ModItems.constructionPaste, 0, currentInv);
        if (slots.size() > 0) {
            for (int slot : slots) {
                ItemStack pasteStack = currentInv.getStackInSlot(slot);
                if (pasteStack.getCount() >= count) {
                    pasteStack.shrink(count);
                    return true;
                }
            }
        }

        List<Integer> containerSlots = findItemClass(GenericPasteContainer.class, currentInv);
        if (containerSlots.size() > 0) {
            for (int slot : containerSlots) {
                ItemStack containerStack = currentInv.getStackInSlot(slot);
                if (containerStack.getItem() instanceof GenericPasteContainer) {
                    int pasteAmt = GenericPasteContainer.getPasteAmount(containerStack);
                    if (pasteAmt >= count) {
                        GenericPasteContainer.setPasteAmount(containerStack, pasteAmt - count);
                        return true;
                    }

                }
            }
        }

        return false;
    }

    private static List<IItemHandler> findInvContainers(EntityPlayer player) {
        List<IItemHandler> containers = new ArrayList<>();

        IItemHandler currentInv = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if( currentInv == null )
            return containers;

        for (int i = 0; i < currentInv.getSlots(); ++i) {
            ItemStack itemStack = currentInv.getStackInSlot(i);
            if (itemStack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                containers.add(itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
        }

        return containers;
    }

    public static int countInContainer(IItemHandler container, Item item, int meta) {
        int count = 0;
        ItemStack tempItem;
        for (int i = 0; i < container.getSlots(); i++) {
            tempItem = container.getStackInSlot(i);
            if (tempItem.getItem() == item && tempItem.getMetadata() == meta) {
                count += tempItem.getCount();
            }
        }
        return count;
    }

    private static List<Integer> findItem(Item item, int meta, IItemHandler itemHandler) {
        List<Integer> slots = new ArrayList<>();
        if( itemHandler == null )
            return slots;

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() == item && meta == stack.getMetadata())
                slots.add(i);
        }
        return slots;
    }

    public static List<Integer> findItemClass(Class c, IItemHandler itemHandler) {
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < itemHandler.getSlots(); i++) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty() && c.isInstance(stack.getItem())) {
                slots.add(i);
            }
        }
        return slots;
    }

    public static ItemStack getSilkTouchDrop(IBlockState state) {
        Item item = Item.getItemFromBlock(state.getBlock());
        int i = 0;
        if (item.getHasSubtypes()) {
            i = state.getBlock().damageDropped(state);
        }
        return new ItemStack(item, 1, i);
    }

    public static IBlockState getSpecificStates(IBlockState originalState, World world, EntityPlayer player, BlockPos pos, ItemStack tool) {
        IBlockState placeState;
        Block block = originalState.getBlock();
        ItemStack item = block.getPickBlock(originalState, null, world, pos, player);
        int meta = item.getMetadata();
        try {
            placeState = originalState.getBlock().getStateForPlacement(world, pos, EnumFacing.UP, 0, 0, 0, meta, player, EnumHand.MAIN_HAND);
        } catch (Exception var8) {
            placeState = originalState.getBlock().getDefaultState();
        }
        for (IProperty prop : placeState.getPropertyKeys()) {
            if (tool.getItem() instanceof GadgetCopyPaste) {
                if (SAFE_PROPERTIES_COPY_PASTE.contains(prop)) {
                    placeState = placeState.withProperty(prop, originalState.getValue(prop));
                }
            } else {
                if (SAFE_PROPERTIES.contains(prop)) {
                    placeState = placeState.withProperty(prop, originalState.getValue(prop));
                }
            }
        }
        return placeState;

    }

    /**
     * Find an item stack in either hand that delegates to the given {@code itemClass}.
     * <p>
     * This method will prioritize primary hand, which means if player hold the desired item on both hands, it will
     * choose his primary hand first. If neither hands have the desired item stack, it will return {@link
     * ItemStack#EMPTY}.
     *
     * @return {@link ItemStack#EMPTY} when neither hands met the parameter.
     */
    public static ItemStack getStackInEitherHand(EntityPlayer player, Class<?> itemClass) {
        ItemStack mainHand = player.getHeldItemMainhand();
        if (itemClass.isInstance(mainHand.getItem()))
            return mainHand;
        ItemStack offhand = player.getHeldItemOffhand();
        if (itemClass.isInstance(offhand.getItem()))
            return offhand;
        return ItemStack.EMPTY;
    }

    public static String formatItemCount(int maxSize, int count) {
        int stacks = count / maxSize; // Integer division automatically floors
        int leftover = count % maxSize;
        if (stacks == 0)
            return String.valueOf(leftover);
        return stacks + "×" + maxSize + "+" + leftover;
    }

    /*public static IBlockState getBaseState(IBlockState originalState, World world, EntityPlayer player, BlockPos pos) {
        IBlockState placeState = Blocks.AIR.getDefaultState();
        Block block = originalState.getBlock();
        placeState = originalState.getBlock().getDefaultState();
        return placeState;
    }*/
}