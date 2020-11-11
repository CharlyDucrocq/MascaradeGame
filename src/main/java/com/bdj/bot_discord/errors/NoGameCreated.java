package com.bdj.bot_discord.errors;

public class NoGameCreated extends GameException{
    public NoGameCreated(){
        super("Aucune partie créée");
    }
}
