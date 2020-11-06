package com.bdj.bot_discord.mascarade_bot.errors;

public abstract class GameException extends RuntimeException {
    GameException(){
        super();
    }
    GameException(String msg){
        super(msg);
    }
}
