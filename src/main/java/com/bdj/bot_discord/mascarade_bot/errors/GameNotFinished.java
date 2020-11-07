package com.bdj.bot_discord.mascarade_bot.errors;

public class GameNotFinished extends GameException {
    public GameNotFinished(){
        super("Vous ne pouvez pas faire ça en cours de partie");
    }
}
