package com.direwolf20.buildinggadgets.client.gui.base.widget;

public interface IReloacatableWidgetWrapper extends IWidgetFactory {

    void setX(int x);

    void setY(int y);

    IWidget unwrap();
}
