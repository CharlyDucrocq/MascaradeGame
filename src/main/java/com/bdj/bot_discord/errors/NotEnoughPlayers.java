package com.bdj.bot_discord.errors;

import com.bdj.bot_discord.games.mascarade.GlobalParameter;

public class NotEnoughPlayers extends GameException {
    public NotEnoughPlayers(int min){
        super("Pas assez de joueur. (min="+ min+")");
    }
}
