package com.direwolf20.buildinggadgets.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;

public class StructureMaterialList extends GuiScrollingList {

    private static final int VERTICAL_PADDING = 10;

    private List<ItemStack> materialList;
    private int selectedIndex;

    public StructureMaterialList(List<ItemStack> materialList, int width, int height, int left, int screenWidth, int screenHeight) {
        super(Minecraft.getMinecraft(), width, height, VERTICAL_PADDING, VERTICAL_PADDING, left, Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 4, screenWidth, screenHeight);
        this.materialList = materialList;
    }

    @Override
    protected int getSize() {
        return materialList.size();
    }

    @Override
    protected void elementClicked(int i, boolean b) {
        this.selectedIndex = i;
    }

    @Override
    protected boolean isSelected(int i) {
        return selectedIndex == i;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int id, int entryRight, int slotTop, int slotBuffer, Tessellator tessellator) {
    }

}
