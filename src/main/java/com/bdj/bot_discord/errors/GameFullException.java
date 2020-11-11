package com.bdj.bot_discord.errors;

import com.bdj.bot_discord.games.mascarade.GlobalParameter;

public class GameFullException extends GameException {
    public GameFullException(){
        super("Le nombre maximum de joueur possible est déjà atteint. (max="+ GlobalParameter.MAX_PLAYERS+")");
    }
}
