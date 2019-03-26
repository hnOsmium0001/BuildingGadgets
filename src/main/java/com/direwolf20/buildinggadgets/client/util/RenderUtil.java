package com.direwolf20.buildinggadgets.client.util;

import com.direwolf20.buildinggadgets.common.BuildingGadgets;
import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderUtil {

    private static final FontRenderer FONT_RENDERER = Minecraft.getMinecraft().fontRenderer;

    /**
     * Size in pixels of an vanilla slot background
     */
    public static final int SLOT_SIZE = 18;
    /**
     * {@link ResourceLocation} of vanilla item slot background.
     */
    public static final ResourceLocation SLOT_BACKGROUND = new ResourceLocation(BuildingGadgets.MODID, "textures/gui/item_slot_background.png");

    public static final int FONT_HEIGHT = FONT_RENDERER.FONT_HEIGHT;

    /**
     * @return Left x value if the box is be aligned to the right
     */
    public static int getXForAlignedRight(int width, int rightX) {
        return rightX - width;
    }

    /**
     * @return Left x value if the box is aligned in the middle
     */
    public static int getXForAlignedCenter(int width, int leftX, int rightX) {
        Preconditions.checkArgument(leftX < rightX);
        return leftX + (rightX - leftX) / 2 - width / 2;
    }

    /**
     * @return Top y value if the box is aligned to the bottom
     */
    public static int getYForAlignedBottom(int height, int bottomY) {
        return bottomY - height;
    }

    /**
     * @return Top y value if the box is aligned in the center
     */
    public static int getYForAlignedCenter(int height, int topY, int bottomY) {
        Preconditions.checkArgument(bottomY > topY);
        return topY + (bottomY - topY) / 2 - height / 2;
    }

    public static void renderTextHorizontalLeft(String text, int leftX, int y, int color) {
        FONT_RENDERER.drawString(text, leftX, y, color);
    }

    public static void renderTextHorizontalRight(String text, int rightX, int y, int color) {
        FONT_RENDERER.drawString(text, getXForAlignedRight(FONT_RENDERER.getStringWidth(text), rightX), y, color);
    }

    public static void renderTextHorizontalMiddle(String text, int leftX, int rightX, int y, int color) {
        FONT_RENDERER.drawString(text, getXForAlignedCenter(FONT_RENDERER.getStringWidth(text), leftX, rightX), y, color);
    }

    public static void renderTextVerticalTop(String text, int x, int topY, int color) {
        FONT_RENDERER.drawString(text, x, topY, color);
    }

    public static void renderTextVerticalBottom(String text, int x, int bottomY, int color) {
        FONT_RENDERER.drawString(text, x, getYForAlignedBottom(FONT_HEIGHT, bottomY), color);
    }

    public static void renderTextVerticalCenter(String text, int x, int topY, int bottomY, int color) {
        FONT_RENDERER.drawString(text, x, getYForAlignedCenter(FONT_HEIGHT, topY, bottomY), color);
    }

}
