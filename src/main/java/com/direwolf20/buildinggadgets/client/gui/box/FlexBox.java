package com.direwolf20.buildinggadgets.client.gui.box;

import com.direwolf20.buildinggadgets.client.gui.IWidget;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.GlStateManager;

public abstract class FlexBox implements IWidget {

    public static FlexBox horiztonal(int x, int y, int width, int height, ImmutableList<IWidget> elements) {
        return new HorizontalFlexBox(x, y, width, height, elements);
    }

    public static FlexBox vertical(int x, int y, int width, int height, ImmutableList<IWidget> elements) {
        return new VerticalFlexBox(x, y, width, height, elements);
    }

    protected ImmutableList<FlexElement> flexElements;

    private int x;
    private int y;
    private int width;
    private int height;

    protected int stretchLength;
    protected int elementsStretchLength;

    FlexBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    abstract void calculateFlexPosition(ImmutableList<IWidget> elements);

    public int getStretchLength() {
        return stretchLength;
    }

    public int getElementsStretchLength() {
        return elementsStretchLength;
    }

    public abstract int getAmountSpacing();

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw() {
        GlStateManager.pushMatrix();
        for (FlexElement flexElement : flexElements) {
            flexElement.draw();
        }
        GlStateManager.popMatrix();
    }

}
