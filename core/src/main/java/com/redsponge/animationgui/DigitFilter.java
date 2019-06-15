package com.redsponge.animationgui;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;

public class DigitFilter implements TextFieldFilter {

    private Character[] accepted;

    public DigitFilter() {
        accepted = new Character[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
    }

    @Override
    public boolean acceptChar(TextField textField, char c) {
        return Utils.arrayContains(accepted, c);
    }
}
