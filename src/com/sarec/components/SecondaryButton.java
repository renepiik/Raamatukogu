package com.sarec.components;

import com.sarec.Vars;
import javafx.scene.control.Button;

public class SecondaryButton extends Button {
    public SecondaryButton(String text) {
        super(text);
        this.setStyle(Vars.ButtonStyleSecondary);
    }
}
