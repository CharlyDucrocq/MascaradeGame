package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;

import java.util.Observable;
import java.util.Observer;

public class Game implements Observer {
    public final int nbStartingTurn; // during only switch available

    private TableRound tableRound;
    private GameRound round;
    private InOutGameInterface inOut;
    private MascaradeOut out;

    public Game(Player[] players){
        tableRound = new TableRound(players);
        nbStartingTurn = players.length;
    }

    public void start() {
        out.printStart(this);
        nextRound();
    }

    void nextRound(){
        if (nbStartingTurn>tableRound.getNbTurnDone())
            round = new StartingRound(out, tableRound.next());
        else
            round = new GameRound(out, tableRound.next());
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
        this.out = new MascaradeOut(inOut);
    }

    public Player getPlayer(User user) {
        return tableRound.getPlayer(user);
    }

    public User[] getUsers() {
        return tableRound.getUsers();
    }
}
