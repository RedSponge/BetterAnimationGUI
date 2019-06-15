package com.redsponge.animationgui;

import com.kotcrab.vis.ui.widget.Menu;
import com.kotcrab.vis.ui.widget.MenuItem;

public class FileMenu extends Menu {

    public FileMenu() {
        super("File");
        addItem(new MenuItem("New"));
    }
}
