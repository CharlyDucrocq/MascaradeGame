package com.bdj.bot_discord.mascarade_bot.errors;

public class NotInTheGame extends GameException {
    public NotInTheGame(){
        super("Vous ne faite pas partie du jeu");
    }
}
