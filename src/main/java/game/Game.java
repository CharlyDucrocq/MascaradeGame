package game;

import discord.User;
import utils.InOutGameInterface;

import java.util.Observable;
import java.util.Observer;

public class Game implements Observer {
    private final int nbRoundForInit = 1;
    private TableRound tableRound;
    private GameRound round;
    private InOutGameInterface inOut;

    public Game(Player[] players){
        tableRound = new TableRound(players);
    }

    public void start() {
        //TODO
    }

    private void nextRound(){

    }

    @Override
    public void update(Observable o, Object arg) {
        if(o == round){
            if(round.isEnded()) nextRound();
        }
    }

    public GameRound getRound() {
        return round;
    }

    public void setInOut(InOutGameInterface inOut) {
        this.inOut = inOut;
    }

    public Player getPlayer(User user) {
        return tableRound.getPlayer(user);
    }

    public User[] getUsers() {
        return tableRound.getUsers();
    }
}
