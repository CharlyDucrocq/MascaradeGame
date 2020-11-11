package com.bdj.bot_discord.errors;

public class BadUser extends GameException {
    public BadUser(){
        super("Ce n'est pas a vous de faire ça !");
    }
}
