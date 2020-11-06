package com.bdj.bot_discord.mascarade_bot.errors;

public class NoGameStarted extends GameException {
    public NoGameStarted(){
        super("La partie n'a pas commencé");
    }
}
