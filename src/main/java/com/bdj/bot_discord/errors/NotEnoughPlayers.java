package com.bdj.bot_discord.errors;

import com.bdj.bot_discord.games.mascarade.GlobalParameter;

public class NotEnoughPlayers extends GameException {
    public NotEnoughPlayers(){
        super("Pas assez de joueur. (min="+ GlobalParameter.MIN_PLAYERS+")");
    }
}
