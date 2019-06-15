package com.redsponge.animationgui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

public class HomeTab extends Tab {

    private Window contentTable;

    public HomeTab() {
        super(false, false);
        contentTable = new Window("Welcome to the Better Animation Editor!", VisUI.getSkin());
    }

    @Override
    public String getTabTitle() {
        return "Home";
    }

    @Override
    public Table getContentTable() {
        return contentTable;
    }
}
