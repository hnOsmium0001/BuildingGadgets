package com.direwolf20.buildinggadgets.client.gui;

import com.direwolf20.buildinggadgets.client.gui.box.FlexBox;
import com.direwolf20.buildinggadgets.client.util.RenderUtil;
import com.direwolf20.buildinggadgets.common.items.Template;
import com.direwolf20.buildinggadgets.common.tools.InventoryManipulation;
import com.direwolf20.buildinggadgets.common.tools.UniqueItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multiset;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is adapted from Lunatrius's Schematica mod, 1.12.2 version.
 * <a href="github.com/Lunatrius/Schematica/blob/master/src/main/java/com/github/lunatrius/schematica/client/gui/control/GuiSchematicMaterials.java">Github</a>
 */
public class TemplateMaterialListGui extends GuiScreen {

    public static final int BUTTON_HEIGHT = 20;
    public static final int BUTTON_WIDTH_MAX = 128;
    public static final int BUTTONS_PADDING = 4;
    public static final int BUTTONS_SIDE_MARGIN_MIN = 32;

    // TODO use a united way to store materials
    private static List<ItemStack> toInternalMaterialList(Multiset<UniqueItem> templateList) {
        List<ItemStack> result = new ArrayList<>();
        for (Multiset.Entry<UniqueItem> entry : templateList.entrySet()) {
            // ItemStack implementation ignores the stack limit
            ItemStack itemStack = new ItemStack(entry.getElement().item, entry.getCount(), entry.getElement().meta);
            result.add(itemStack);
        }
        return result;
    }

    private ItemStack template;
    private List<ItemStack> materials;
    private IntList available;
    private MaterialList scrollingList;
    // private FlexBox buttons;

    private String title;
    private int titleLeft;
    private int titleTop;

    public TemplateMaterialListGui(ItemStack template) {
        this.template = template;
    }

    @Override
    public void initGui() {
        Template item = (Template) template.getItem();

        this.materials = toInternalMaterialList(item.getItemCountMap(template));
        this.updateAvailableMaterials();
        this.scrollingList = new MaterialList(this, this.materials, this.available, this.width, this.height);

        // int unit = width / 8;
        // this.buttons = FlexBox.horiztonal(unit, MaterialList.BOTTOM / 2 - BUTTON_HEIGHT / 2, unit * 6, 20, ImmutableList.of());

        this.title = "Material List";
        this.titleTop = MaterialList.TOP / 2 - fontRenderer.FONT_HEIGHT / 2;
        this.titleLeft = (width / 2) - (fontRenderer.getStringWidth(title) / 2);

        int buttonID = -1;
        int buttonY = height - (MaterialList.BOTTOM / 2 + BUTTON_HEIGHT / 2);
        this.addButton(new DireButton(++buttonID, 0, buttonY, 50, BUTTON_HEIGHT, "Refresh Count"));
        this.addButton(new DireButton(++buttonID, 0, buttonY, 50, BUTTON_HEIGHT, "Close"));
        this.calculateWidthAndX();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particleTicks) {
        drawDefaultBackground();

        this.scrollingList.drawScreen(mouseX, mouseY, particleTicks);
        // this.buttons.draw();
        this.drawString(fontRenderer, title, titleLeft, titleTop, Color.WHITE.getRGB());
        super.drawScreen(mouseX, mouseY, particleTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollingList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                updateAvailableMaterials();
                return;
            case 1:
                Minecraft.getMinecraft().player.closeScreen();
                return;
        }
        this.scrollingList.actionPerformed(button);
    }

    /**
     * {@inheritDoc}
     * Override to make it visible to outside.
     */
    @Override
    public void renderToolTip(ItemStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }

    private void updateAvailableMaterials() {
        this.available = InventoryManipulation.countItems(materials, Minecraft.getMinecraft().player);
    }

    private void calculateWidthAndX() {
        int availableWidth = width - BUTTONS_SIDE_MARGIN_MIN * 2;

        int amountButtons = buttonList.size();
        int amountPadding = amountButtons - 1;
        int paddingWidth = amountPadding * BUTTONS_PADDING;
        int buttonWidth = MathHelper.clamp((availableWidth - paddingWidth) / amountButtons, 1, BUTTON_WIDTH_MAX);

        int boxWidth = buttonWidth * amountButtons + paddingWidth * amountPadding;
        int nextX = RenderUtil.getXForAlignedCenter(boxWidth, 0, width);
        for (GuiButton button : buttonList) {
            button.width = buttonWidth;
            button.x = nextX;
            nextX += buttonWidth + paddingWidth;
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
