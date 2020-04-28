package com.sarec;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.filechooser.FileSystemView;

public class Vars {
    // kaust, kus hoitakse raamatukogude infot
    public static String libsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath()+"\\libs\\";

    public static String ButtonColor = "#6246ea";
    public static Paint ButtonPaint = Color.web(ButtonColor);

    public static String BackgroundColor = "#fffffe";
    public static Paint BackgroundPaint = Color.web(BackgroundColor);

    public static String DarkTextColor = "#2b2c34";
    public static Paint DarkTextPaint = Color.web(DarkTextColor);

    public static String LightTextColor = "#fffffe";
    public static Paint LightTextPaint = Color.web(LightTextColor);

    public static String SecondaryColor = "#d1d1e9";
    public static Paint SecondaryPaint = Color.web(SecondaryColor);

    public static String TertiaryColor = "#e45858";
    public static Paint TertiaryPaint = Color.web(TertiaryColor);

    public static String ButtonStylePrimary = "-fx-background-color: "+ ButtonColor +"; -fx-border-width: 0px; -fx-font-size: 1.1em; -fx-text-fill: "+LightTextColor+";";
    public static String ButtonStyleSecondary = "-fx-background-color: "+ SecondaryColor +"; -fx-font-size: 1.1em; -fx-text-fill: "+DarkTextColor+";";
}