package com.bdj.bot_discord.games.times_bomb;

public enum Card {
    CABLE("Nouveau cable désamorcé !"),
    FAKE("Faux cable.. La partie continue"),
    BOMB("BOOOOOOOUM ! Les "+Team.MORIARTY+"s ont fait explosé leur bombe !"),
    ;
    String result;

    Card(String txt){
        result =txt;
    }

    public String getResultMsg() {
        return result;
    }

    public String getIconUrl() {
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/times_bomb_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }
}
