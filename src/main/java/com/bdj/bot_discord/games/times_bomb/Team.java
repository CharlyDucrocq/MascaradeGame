package com.bdj.bot_discord.games.times_bomb;

public enum  Team {
    SHERLOCK,
    MORIARTY,
    ;

    public String getIconUrl(){
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/times_bomb_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }
}
