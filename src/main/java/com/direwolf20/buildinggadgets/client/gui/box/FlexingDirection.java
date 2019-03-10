package com.direwolf20.buildinggadgets.client.gui.box;

import com.direwolf20.buildinggadgets.client.gui.IWidget;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;

public enum FlexingDirection {

    LEFT(Axis.X, -1),
    RIGHT(Axis.X, +1),
    UP(Axis.Y, +1),
    DOWN(Axis.Y, -1);

    private final Axis axis;
    private final int offset;

    FlexingDirection(Axis axis, int offset) {
        this.axis = axis;
        this.offset = offset;
    }

    public Axis getAxis() {
        return axis;
    }

    public int getOffset() {
        return offset;
    }

}
