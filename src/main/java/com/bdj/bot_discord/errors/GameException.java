package com.bdj.bot_discord.errors;

public class GameException extends RuntimeException {
    public GameException(){
        super();
    }
    public GameException(String msg){
        super(msg);
    }
}
