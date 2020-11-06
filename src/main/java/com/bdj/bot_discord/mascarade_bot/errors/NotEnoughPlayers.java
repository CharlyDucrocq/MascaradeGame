package com.bdj.bot_discord.mascarade_bot.errors;

public class NotEnoughPlayers extends GameException {
    NotEnoughPlayers(){
        super("Pas assez de joueur");
    }
}
