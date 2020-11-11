package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.Player;

public abstract class Card {
    protected final Character type;
    final Player player;

    public Card(Character type, Player player){
        this.type = type;
        this.player = player;
    }

    public abstract void action();

    public Character getType() {
        return type;
    }

    public String getDescription(){
        return type.getDescription();
    }
}
