package com.direwolf20.buildinggadgets.client.gui.box;

import com.direwolf20.buildinggadgets.client.gui.IWidget;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Comparator;

class VerticalFlexBox extends FlexBox {

    VerticalFlexBox(int x, int y, int width, int height, ImmutableList<IWidget> elements) {
        super(x, y, width, height);

        this.stretchLength = y;
        this.elementsStretchLength = elements.stream()
                .mapToInt(IWidget::getHeight)
                .sum();

        int maxWidth = elements.stream()
                .max(Comparator.comparing(IWidget::getWidth))
                .orElseThrow(IllegalArgumentException::new)
                .getWidth();
        Preconditions.checkArgument(width >= maxWidth);

        this.calculateFlexPosition(elements);
    }

    @Override
    void calculateFlexPosition(ImmutableList<IWidget> elements) {

    }

    @Override
    public int getAmountSpacing() {
        return 0;
    }

}
