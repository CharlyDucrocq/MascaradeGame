package com.bdj.bot_discord.errors;

public class InvalidCommand extends GameException {
    public InvalidCommand(){
        super("Commande invalide");
    }
}
