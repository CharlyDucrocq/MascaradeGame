package com.bdj.bot_discord.mascarade_bot.errors;

import com.bdj.bot_discord.mascarade_bot.game.GlobalParameter;

public class GameFullException extends GameException {
    public GameFullException(){
        super("Le nombre maximum de joueur possible est déjà atteint. (max="+ GlobalParameter.MAX_PLAYERS+")");
    }
}
