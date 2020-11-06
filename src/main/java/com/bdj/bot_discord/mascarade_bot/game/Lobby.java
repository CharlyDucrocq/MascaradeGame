package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.GameFullException;

import java.util.LinkedList;
import java.util.List;

public class Lobby {
    List<User> users = new LinkedList<>();
    User admin;

    public void addPlayer(User user){
        if(users.size()==GlobalParameter.MAX_PLAYERS) throw new GameFullException();
        users.add(user);
    }

    public boolean removePlayer(User user){
        return users.remove(user);
    }

    public boolean contain(User user){
        return users.contains(user);
    }

    public void setAdmin(User user){
        admin = user;
    }

    public Game createGame(){
        List<Player> players = new LinkedList<>();
        for (User user : this.users) players.add(new Player(user));
        return new GameFactory(players).createGame();
    }

    public boolean haveEnoughPlayer(){
        return users.size()>=GlobalParameter.MIN_PLAYERS;
    }

    public boolean isAdmin(User user) {
        return user.equals(admin);
    }
}
