package com.direwolf20.buildinggadgets.client.gui.box;

import com.direwolf20.buildinggadgets.client.gui.IWidget;
import net.minecraft.client.renderer.GlStateManager;

final class FlexElement implements IWidget {

    private final IWidget handle;
    private final int x;
    private final int y;

    public FlexElement(IWidget handle, int x, int y) {
        this.handle = handle;
        this.x = x;
        this.y = y;
    }

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
        return handle.getWidth();
    }

    @Override
    public int getHeight() {
        return handle.getHeight();
    }

    @Override
    public void draw() {
        GlStateManager.translate(x, y, 0);
        handle.draw();
    }

}
