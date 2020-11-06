package com.bdj.bot_discord.mascarade_bot.errors;

import com.bdj.bot_discord.mascarade_bot.discord.User;

public class BadUser extends GameException {
    public BadUser(){
        super("Ce n'est pas a vous de faire ça !");
    }
}
