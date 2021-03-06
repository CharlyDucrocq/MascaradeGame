package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.games.mascarade.card.Character;

import java.util.List;

public class MascaradeGame implements Game {
    private Thread gameThread;

    private final List<Character> characters;
    public int nbStartingTurn; // during only switch available

    private TableRound tableRound;
    private GameRound round;
    private MascaradeOut out;

    public boolean ended = false;

    private Bank bank = new Bank();

    public MascaradeGame(Player[] players, List<Character> characters){
        tableRound = new TableRound(players);
        this.characters = characters;
        nbStartingTurn = players.length;
    }

    public void disableStartingTurn(){
        this.nbStartingTurn = 0;
    }

    public void start() {
        out.printStart(this);
        this.gameThread = new Thread(this::nextRound){{start();}};
    }

    @Override
    public void kill() {
        this.gameThread.stop(); //TODO : find another way
        this.ended = true;
    }

    void nextRound(){
        if(ended()){
            endGame();
            return;
        }
        if (isInPreliminary())
            round = new StartingRound(this, tableRound.next());
        else
            round = new GameRound(this, tableRound.next());
        round.start();
        nextRound();
    }

    private void endGame() {
        ended = true;
        out.printEnd(this);
        out.printPodium(tableRound);
    }

    private boolean ended() {
        if (this.ended) return true;
        for(Player player : tableRound.getPlayers()){
            if (player.endTheGame()) return true;
        }
        return false;
    }

    public GameRound getRound() {
        return round;
    }

    public void setOut(MascaradeOut out) {
        this.out = out;
    }

    public Player getPlayer(User user) {
        return tableRound.getPlayer(user);
    }

    public User[] getUsers() {
        return tableRound.getUsers();
    }

    public List<Character> getCharactersList() {
        return characters;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBankOnlyForTest(Bank bank) {
        this.bank = bank;
    }

    public TableRound getTable() {
        return tableRound;
    }

    public MascaradeOut getOut() {
        return out;
    }

    public boolean isInPreliminary() {
        return nbStartingTurn>tableRound.getNbTurnDone();
    }

    public boolean isOver() {
        return ended;
    }


}
