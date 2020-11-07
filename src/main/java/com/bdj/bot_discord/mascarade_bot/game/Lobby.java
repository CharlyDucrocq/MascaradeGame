package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.InOutDiscord;
import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.GameFullException;
import com.bdj.bot_discord.mascarade_bot.errors.GameNotFinished;
import com.bdj.bot_discord.mascarade_bot.errors.NotEnoughPlayers;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;

import java.util.LinkedList;
import java.util.List;

public class Lobby {
    List<User> users = new LinkedList<>();
    User admin;
    Game game;
    final InOutDiscord inOut;

    public Lobby(){
        inOut = new InOutDiscord();
    }

    public Lobby(InOutDiscord inOutMock) {
        inOut = inOutMock;
    }

    public void addPlayer(User user){
        if(users.size()==GlobalParameter.MAX_PLAYERS) throw new GameFullException();
        users.add(user);
        inOut.printGlobalMsg(user.toString()+" a rejoin la partie");
    }

    public void removePlayer(User user){
        if (users.remove(user)) inOut.printGlobalMsg(user.toString()+" a quitté la partie");
    }

    public boolean contain(User user){
        return users.contains(user);
    }

    public void setAdmin(User user){
        admin = user;
    }

    public void createGame(){
        if(!haveEnoughPlayer()) throw new NotEnoughPlayers();
        List<Player> players = new LinkedList<>();
        for (User user : this.users) players.add(new Player(user));
        game = new GameFactory(players).createGame();
        game.setInOut(inOut);
    }

    public boolean haveEnoughPlayer(){
        return users.size()>=GlobalParameter.MIN_PLAYERS;
    }

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

    public Game getGame() {
        return game;
    }

    public InOutDiscord getInOut() {
        return inOut;
    }

    public boolean isPlayer(User user) {
        return users.contains(user);
    }
}
