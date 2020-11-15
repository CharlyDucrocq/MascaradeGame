package com.bdj.bot_discord.games.code_name;

import com.bdj.bot_discord.discord.User;

public class Player {
    User user;
    Player(User user){
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
