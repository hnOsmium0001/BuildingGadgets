package com.direwolf20.buildinggadgets.client.gui;

import com.direwolf20.buildinggadgets.common.BuildingGadgets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class MaterialList extends GuiSlot {

    private static final ResourceLocation SLOT_BACKGROUND = new ResourceLocation(BuildingGadgets.MODID, "textures/gui/item_slot_background.png");

    public static final int SLOT_SIZE = 18;
    public static final int MARGIN = 2;
    public static final int ENTRY_HEIGHT = SLOT_SIZE + MARGIN * 2;
    public static final int TOP = 24;
    public static final int BOTTOM = 32;

    private TemplateMaterialListGui parent;
    private List<ItemStack> materialList;

    public MaterialList(TemplateMaterialListGui parent, List<ItemStack> materialList, int width, int height) {
        super(Minecraft.getMinecraft(), width, height, TOP, height - BOTTOM, ENTRY_HEIGHT);
        this.parent = parent;
        this.materialList = materialList;
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
    protected void drawSlot(int id, int xPos, int yPos, int height, int mouseX, int mouseY, float partialTicks) {
        ItemStack item = materialList.get(id);

        int slotX = xPos + MARGIN;
        int slotY = yPos + MARGIN;

        //TODO add slot background
//        GlStateManager.enableTexture2D();
//        GlStateManager.disableLighting();
//        GlStateManager.disableDepth();
//        GlStateManager.color(1, 1, 1);
//        Minecraft.getMinecraft().getTextureManager().bindTexture(SLOT_BACKGROUND);
//        parent.drawTexturedModalRect(slotX, slotY, 0, 0, SLOT_SIZE, SLOT_SIZE);

        GlStateManager.pushMatrix();
        RenderHelper.enableGUIStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(item, slotX, slotY - 1);
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();

        String itemName = item.getDisplayName();
        int amount = item.getCount();

        //

        if (mouseX > slotX && mouseY > slotY && mouseX <= slotX + 18 && mouseY <= slotY + 18) {
            parent.renderToolTip(item, mouseX, mouseY);
        }
    }

}
