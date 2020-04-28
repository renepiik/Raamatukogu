package com.sarec.components;

import com.sarec.Vars;
import javafx.scene.control.Button;

public class PrimaryButton extends Button{
    public PrimaryButton(String text) {
        super(text);
        this.setStyle(Vars.ButtonStylePrimary);
    }
}
