package com.direwolf20.buildinggadgets.client.gui;

import com.direwolf20.buildinggadgets.common.items.Template;
import com.direwolf20.buildinggadgets.common.tools.UniqueItem;
import com.google.common.collect.Multiset;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

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

    private StructureMaterialList materialList;

    @Override
    public void initGui() {
        super.initGui();

        EntityPlayer player = Minecraft.getMinecraft().player;
        ItemStack stack = player.getHeldItemMainhand();
        if (!(stack.getItem() instanceof Template)) {
            player.closeScreen();
            return;
        }
        Template template = (Template) stack.getItem();
        List<ItemStack> materials = toInternalMaterialList(template.getItemCountMap(stack));

        this.materialList = new StructureMaterialList(materials, this.width, this.height - (16 * 2), 0, this.width, this.height);

        int buttonID = -1;
        this.addButton(new DireButton(++buttonID, 0, 0, 50, 10, "Close"));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float particleTicks) {
        super.drawDefaultBackground();

        this.materialList.drawScreen(mouseX, mouseY, particleTicks);

        super.drawScreen(mouseX, mouseY, particleTicks);
    }

}
