package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.GameFullException;

import java.util.LinkedList;
import java.util.List;

public class Lobby {
    List<Player> players = new LinkedList<>();
    GameFactory gameFactory = new GameFactory(players);
    Player admin;

    public void addPlayer(User user){
        if(players.size()==GlobalParameter.MAX_PLAYERS) throw new GameFullException();
        players.add(new Player(user));
    }

    public void addAdmin(User user){
        admin = new Player(user);
        players.add(admin);
    }

    public Game createGame(){
        return gameFactory.createGame();
    }

    public boolean haveEnoughPlayer(){
        return players.size()>=GlobalParameter.MIN_PLAYERS;
    }

    public boolean isAdmin(User user) {
        return user.equals(admin.getUser());
    }
}
