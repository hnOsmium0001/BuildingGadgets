package com.direwolf20.buildinggadgets.client.gui;

import com.direwolf20.buildinggadgets.client.util.RenderUtil;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.util.List;

import static com.direwolf20.buildinggadgets.client.util.RenderUtil.FONT_HEIGHT;
import static com.direwolf20.buildinggadgets.client.util.RenderUtil.SLOT_SIZE;

public class MaterialList extends GuiSlot {

    static final int MARGIN = 2;
    static final int ENTRY_HEIGHT = Math.max(SLOT_SIZE + MARGIN * 2, FONT_HEIGHT * 2 + MARGIN * 3);
    static final int TOP = 24;
    static final int BOTTOM = 32;

    //TODO calculate them based on font height
    private static final int TEXT_STATUS_Y_OFFSET = 0;
    private static final int TEXT_AMOUNT_Y_OFFSET = 12;

    private TemplateMaterialListGui parent;
    private List<ItemStack> materialList;
    private IntList available;

    public MaterialList(TemplateMaterialListGui parent, List<ItemStack> materialList, IntList available, int width, int height) {
        super(Minecraft.getMinecraft(), width, height, TOP, height - BOTTOM, ENTRY_HEIGHT);
        this.parent = parent;
        this.materialList = materialList;
        this.available = available;
    }

    @Override
    protected int getSize() {
        return this.materialList.size();
    }

    @Override
    protected void elementClicked(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        this.selectedElement = slotIndex;
    }

    @Override
    protected boolean isSelected(int slotIndex) {
        return this.selectedElement == slotIndex;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int id, int left, int top, int height, int mouseX, int mouseY, float partialTicks) {
        ItemStack item = this.materialList.get(id);
        // For some reason selection box is the width as entryWidth (did not consider border)
        int right = left + getListWidth() - 5;
        int bottom = top + ENTRY_HEIGHT;

        int slotX = left + MARGIN;
        int slotY = top + MARGIN;

        //TODO add slot background
//        GlStateManager.enableTexture2D();
//        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
//        GlStateManager.color(1, 1, 1);
//        Minecraft.getMinecraft().getTextureManager().bindTexture(RenderUtil.SLOT_BACKGROUND);
//        parent.drawTexturedModalRect(slotX, slotY, 0, 0, SLOT_SIZE, SLOT_SIZE);

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, slotX, slotY);
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();

        String itemName = item.getDisplayName();
        int itemNameX = slotX + SLOT_SIZE + MARGIN;
        // -1 because the bottom x coordinate is exclusive
        RenderUtil.renderTextVerticalCenter(itemName, itemNameX, top, bottom - 1, Color.WHITE.getRGB());

        int required = item.getCount();
        int available = MathHelper.clamp(this.available.getInt(id), 0, required);
        boolean fulfilled = available == required;
        int color = fulfilled ? Color.GREEN.getRGB() : Color.RED.getRGB();
        String amount = available + "/" + required;
        String status = fulfilled ? "Available" : "Missing";
        RenderUtil.renderTextHorizontalRight(status, right, top + TEXT_STATUS_Y_OFFSET, color);
        RenderUtil.renderTextHorizontalRight(amount, right, top + TEXT_AMOUNT_Y_OFFSET, Color.WHITE.getRGB());

        if (mouseX > slotX && mouseY > slotY && mouseX <= slotX + 18 && mouseY <= slotY + 18) {
            parent.renderToolTip(item, mouseX, mouseY);
            GlStateManager.disableLighting();
        }
    }

}
