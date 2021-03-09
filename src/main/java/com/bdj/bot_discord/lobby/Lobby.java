package com.bdj.bot_discord.lobby;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.errors.GameFullException;
import com.bdj.bot_discord.errors.NotEnoughPlayers;
import com.bdj.bot_discord.games.mascarade.GlobalParameter;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.*;

public abstract class Lobby<G extends Game> {
    private static int MAX_ID= 1;
    private final int ID = MAX_ID++;

    int maxPlayer;
    List<User> users = new LinkedList<>();
    Set<User> admins = new HashSet<>();
    G game;

    public Lobby(int max){
        maxPlayer = max;
    }

    public void addPlayer(User user){
        if(users.size()== maxPlayer) throw new GameFullException(maxPlayer);
        users.add(user);
    }

    public boolean removePlayer(User user){
        return  users.remove(user);
    }

    public boolean contain(User user){
        return users.contains(user);
    }

    public void addAdmin(User user){
        admins.add(user);
    }

    public G createGame(GameFactory<? extends G> factory){
        factory.setPlayers(users);
        setInOut(factory);
        if(!factory.haveEnoughPlayer()) throw new NotEnoughPlayers(factory.getMinPlayer());
        game =factory.createGame();
        return game;
    }

    protected abstract void setInOut(GameFactory<? extends G> factory);

    public boolean isAdmin(User user) {
        return admins.contains(user);
    }

    public List<User> getUsers() {
        return users;
    }

    public Collection<User> getAdmins() {
        return admins;
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

    public void killGame(){
        if(game!=null) game.kill();
        this.game=null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lobby)) return false;
        Lobby<?> lobby = (Lobby<?>) o;
        return ID == lobby.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }

    @Override
    public String toString() {
        return String.valueOf(ID);
    }

    public void killLobby(){

    }
}
