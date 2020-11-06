package com.bdj.bot_discord.mascarade_bot.errors;

public class NoGameCreated extends GameException{
    public NoGameCreated(){
        super("Aucune partie créée");
    }
}
