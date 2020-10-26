package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Player;

public abstract class Card {
    final Character type;
    final Player player;

    public Card(Character type, Player player){
        this.type = type;
        this.player = player;
    }

    public abstract void action();
}
