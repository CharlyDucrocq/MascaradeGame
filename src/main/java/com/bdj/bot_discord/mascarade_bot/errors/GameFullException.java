package com.bdj.bot_discord.mascarade_bot.errors;

public class GameFullException extends GameException {
    public GameFullException(){
        super("Le nombre maximum de joueur possible est déjà atteint.");
    }
}
