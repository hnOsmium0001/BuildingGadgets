package com.direwolf20.buildinggadgets.client.gui.base.widget;

public interface IWidgetFactory {

    void setX(int x);

    void setY(int y);

    IWidget create();
}
