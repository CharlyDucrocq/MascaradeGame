package com.bdj.bot_discord.errors;

import com.bdj.bot_discord.games.mascarade.GlobalParameter;

public class GameFullException extends GameException {
    public GameFullException(int max){
        super("Le nombre maximum de joueur possible est déjà atteint. (max="+ max+")");
    }
}
