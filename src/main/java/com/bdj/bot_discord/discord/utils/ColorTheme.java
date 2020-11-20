package com.bdj.bot_discord.discord.utils;

import java.awt.*;

public enum ColorTheme{
    DEFAULT(Color.darkGray),
    ERROR(Color.RED),
    INFO(Color.GREEN),
    ;

    private final Color color;

    ColorTheme(Color color){
        this.color =color;
    }

    public Color getColor() {
        return color;
    }
}
