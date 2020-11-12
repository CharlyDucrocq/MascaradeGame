package com.bdj.bot_discord.lobby;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.errors.GameFullException;
import com.bdj.bot_discord.errors.NotEnoughPlayers;
import com.bdj.bot_discord.games.mascarade.GlobalParameter;

import java.util.LinkedList;
import java.util.List;

public abstract class Lobby<G extends Game> {
    int maxPlayer;
    List<User> users = new LinkedList<>();
    User admin;
    G game;

    public Lobby(int max){
        maxPlayer = max;
    }

    public void addPlayer(User user){
        if(users.size()== GlobalParameter.MAX_PLAYERS) throw new GameFullException();
        users.add(user);
    }

    public boolean removePlayer(User user){
        return  users.remove(user);
    }

    public boolean contain(User user){
        return users.contains(user);
    }

    public void setAdmin(User user){
        admin = user;
    }

    public G createGame(GameFactory<? extends G> factory){
        factory.setPlayers(users);
        setInOut(factory);
        if(!factory.haveEnoughPlayer()) throw new NotEnoughPlayers();
        return factory.createGame();
    }

    protected abstract void setInOut(GameFactory<? extends G> factory);

    public boolean isAdmin(User user) {
        return user.equals(admin);
    }

    public List<User> getUsers() {
        return users;
    }

    public User getAdmin() {
        return admin;
    }

    public boolean gameOver() {
        return game == null || game.isOver();
    }

    public G getGame() {
        return game;
    }

    public boolean isPlayer(User user) {
        return users.contains(user);
    }
}
