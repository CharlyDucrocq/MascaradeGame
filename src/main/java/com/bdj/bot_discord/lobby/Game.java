package com.bdj.bot_discord.lobby;

import com.bdj.bot_discord.discord.User;

public interface Game {
    void start();
    public User[] getUsers();
    public boolean isOver();
}