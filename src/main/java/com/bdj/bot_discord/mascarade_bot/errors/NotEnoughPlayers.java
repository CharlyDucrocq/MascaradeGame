package com.bdj.bot_discord.mascarade_bot.errors;

import com.bdj.bot_discord.mascarade_bot.game.GlobalParameter;

public class NotEnoughPlayers extends GameException {
    public NotEnoughPlayers(){
        super("Pas assez de joueur. (min="+ GlobalParameter.MIN_PLAYERS+")");
    }
}
