package com.direwolf20.buildinggadgets.client.gui;

import com.direwolf20.buildinggadgets.common.items.Template;
import com.direwolf20.buildinggadgets.common.tools.UniqueItem;
import com.google.common.collect.Multiset;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is adapted from Lunatrius's Schematica mod, 1.12.2 version.
 * <a href="github.com/Lunatrius/Schematica/blob/master/src/main/java/com/github/lunatrius/schematica/client/gui/control/GuiSchematicMaterials.java">Github</a>
 */
public class TemplateMaterialListGui extends GuiScreen {

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
    private List<ItemStack> available;
    private MaterialList scrollingList;

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
//        this.available = toInternalMaterialList();
        this.scrollingList = new MaterialList(this, materials, this.width, this.height);

        this.title = "Material List";
        this.titleTop = MaterialList.TOP / 2 - fontRenderer.FONT_HEIGHT / 2;
        this.titleLeft = (width / 2) - (fontRenderer.getStringWidth(title) / 2);

        int buttonID = -1;
        this.addButton(new DireButton(++buttonID, 0, 0, 50, 10, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particleTicks) {
        drawDefaultBackground();

        this.scrollingList.drawScreen(mouseX, mouseY, particleTicks);
        this.drawString(fontRenderer, title, titleLeft, titleTop, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, particleTicks);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.scrollingList.handleMouseInput();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        this.scrollingList.actionPerformed(button);
//        switch (button.id) {
//            case 0:
//                Minecraft.getMinecraft().player.closeScreen();
//                return;
//            //TODO refresh item count
//            case 1:
//        }

    }

    @Override
    public void renderToolTip(ItemStack stack, int x, int y) {
        super.renderToolTip(stack, x, y);
    }

}
