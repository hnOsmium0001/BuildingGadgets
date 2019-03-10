package com.direwolf20.buildinggadgets.client.gui.box;

import com.direwolf20.buildinggadgets.client.gui.IWidget;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Comparator;

class HorizontalFlexBox extends FlexBox {

    public HorizontalFlexBox(int x, int y, int width, int height, ImmutableList<IWidget> elements) {
        super(x, y, width, height);
        this.stretchLength = x;
        this.elementsStretchLength = elements.stream()
                .mapToInt(IWidget::getWidth)
                .sum();

        int maxHeight = elements.stream()
                .max(Comparator.comparing(IWidget::getHeight))
                .orElseThrow(IllegalArgumentException::new)
                .getHeight();
        Preconditions.checkArgument(height >= maxHeight);

        this.calculateFlexPosition(elements);
    }

    @Override
    void calculateFlexPosition(ImmutableList<IWidget> elements) {
        ImmutableList.Builder<FlexElement> builder = ImmutableList.builder();
        int averageSpacing = (getStretchLength() - getElementsStretchLength()) / getAmountSpacing();

        int next = getX();
        for (IWidget element : elements) {
            builder.add(new FlexElement(element, next, getY()));
            next += element.getWidth() + averageSpacing;
        }

        this.flexElements = builder.build();
    }

    @Override
    public int getAmountSpacing() {
        return 0;
    }

}
