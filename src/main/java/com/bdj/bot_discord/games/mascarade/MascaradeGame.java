package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.games.mascarade.card.Character;

import java.util.LinkedList;
import java.util.List;

public class MascaradeGame implements Game {
    public final int nbStartingTurn; // during only switch available

    private TableRound tableRound;
    private GameRound round;
    private MascaradeOut out;

    public boolean ended = false;

    private Bank bank = new Bank();

    public MascaradeGame(Player[] players){
        tableRound = new TableRound(players);
        nbStartingTurn = players.length;
    }

    public void start() {
        out.printStart(this);
        nextRound();
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

    public void update(Object o) {
        if(o == round){
            if(round.isEnded()) nextRound();
        }
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
        List<Character> result = new LinkedList<>();
        for (Player player : tableRound.getPlayers()) if(!result.contains(player.getCurrentCharacter())) result.add(player.getCurrentCharacter());
        return result;
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
