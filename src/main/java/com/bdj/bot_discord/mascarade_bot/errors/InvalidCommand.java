package com.bdj.bot_discord.mascarade_bot.errors;

public class InvalidCommand extends GameException {
    public InvalidCommand(){
        super("Commande invalide");
    }
}
