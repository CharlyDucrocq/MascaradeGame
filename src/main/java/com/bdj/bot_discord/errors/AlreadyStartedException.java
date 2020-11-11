package com.bdj.bot_discord.errors;

public class AlreadyStartedException extends GameException {
    public AlreadyStartedException(){
        super("La partie est déja en cours.");
    }
}
