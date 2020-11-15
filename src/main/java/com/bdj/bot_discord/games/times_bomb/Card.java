package com.bdj.bot_discord.games.times_bomb;

import java.awt.*;

public enum Card {
    CABLE(Team.SHERLOCK.color,"Nouveau cable désamorcé !"),
    FAKE(Color.darkGray, "Faux cable.. La partie continue"),
    BOMB(Team.MORIARTY.color, "BOOOOOOOUM ! Les "+Team.MORIARTY+"s ont fait explosé leur bombe !"),
    ;


    private String result;
    public final Color color;

    Card(Color color, String txt){
        result =txt;
        this.color = color;
    }

    public String getResultMsg() {
        return result;
    }

    public Color getColor() {
        return color;
    }

    public String getIconUrl() {
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/times_bomb_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }
}
