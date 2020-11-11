package com.bdj.bot_discord.errors;

public class GameNotFinished extends GameException {
    public GameNotFinished(){
        super("Vous ne pouvez pas faire ça en cours de partie");
    }
}
