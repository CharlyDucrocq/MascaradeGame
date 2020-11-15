package com.bdj.bot_discord.games.times_bomb;

import java.awt.*;

public enum  Team {
    SHERLOCK(Color.blue),
    MORIARTY(Color.red),
    ;

    public final Color color;

    Team(Color color) {
        this.color = color;
    }

    public String getIconUrl(){
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/times_bomb_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }

    public Color getColor() {
        return color;
    }
}
