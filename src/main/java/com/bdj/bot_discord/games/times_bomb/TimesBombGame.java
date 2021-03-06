package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TimesBombGame implements Game {
    private TimesBombOut out;
    public final int minBad;
    public final int maxBad;
    private Random random = new Random();

    private boolean gameOver = false;
    private Team winner;
    private LinkedList<Player> players;
    private Deck cardsLeft;

    private Player prevPlayer = null;
    private Player currentPlayer;

    private int nbCableCut =0;
    private Round round;
    private List<Round> rounds = new LinkedList<>();

    public TimesBombGame(Player[] players, int minBad, int maxBad) {
        this.maxBad = maxBad;
        this.minBad = minBad;
        int nbBad = maxBad-minBad ==0 ? maxBad : (random.nextInt(maxBad-minBad+1)+minBad);
        List<Player> start = new LinkedList<>(Arrays.asList(players));

        LinkedList<Player> shorted = new LinkedList<>();
        for (int i=0;i<nbBad;i++) {
            Player newBad = start.remove(random.nextInt(start.size()));
            newBad.setTeam(Team.MORIARTY);
            shorted.add(newBad);
        }
        shorted.addAll(start);
        this.players = new LinkedList<>();
        int i=0;
        for (Player player : shorted) this.players.add(random.nextInt(++i), player);
        this.currentPlayer = getFirstToPlay();
        this.cardsLeft = new Deck(players.length);
    }

    public void setOut(TimesBombOut out) {
        this.out = out;
    }

    private Player getFirstToPlay() {
        return players.get(new Random().nextInt(players.size()));
    }

    @Override
    public void start() {
        out.printStart(this);
        this.round = new Round(this.players, cardsLeft, out);
        playersTurn();
    }

    void playersTurn() {
        out.turnStart(this);
        out.askForTarget(this);
    }

    void cutCard(Player target, int targetedCard){
        Card card = target.cutCard(targetedCard);
        round.newTurn(currentPlayer, target, card);

        switch (card){
            case CABLE: {
                if(++nbCableCut >=cardsLeft.NB_CABLE) gameOver=true;
                winner = Team.SHERLOCK;
                break;
            }
            case BOMB: {
                gameOver = true;
                winner = Team.MORIARTY;
                break;
            }
            case FAKE:
                break;
        }

        out.printCut(currentPlayer, target, card);

        if (gameOver) {
            endGame();
            return;
        }

        if (round.over()) {
            rounds.add(round);
            if (round.isTheLast()) {
                winner = Team.MORIARTY;
                endGame();
                return;
            }
            for (Player player : players) cardsLeft.addAll(player.getBackCardLeft());
            round = new Round(players, cardsLeft, out);
        }

        prevPlayer = currentPlayer;
        currentPlayer = target;
        playersTurn();
    }

    private void endGame() {
        gameOver = true;
        out.printEndGame(this);
    }

    List<Player> getCuteablePlayer(){
        LinkedList<Player> cuteable = new LinkedList<>(players);
        cuteable.remove(prevPlayer);
        cuteable.remove(currentPlayer);
        return cuteable;
    }

    public List<Round> getRounds() {
        return rounds;
    }

    @Override
    public User[] getUsers() {
        User[] users = new User[players.size()];
        int i=0;
        for (Player player :players)users[i++]=player.getUser();
        return users;
    }

    @Override
    public boolean isOver() {
        return gameOver;
    }

    @Override
    public void kill() {
        this.out.printKill();
        this.out.mute();
        this.gameOver=true;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public LinkedList<Player> getPlayers() {
        return players;
    }

    public int nbCableLeft() {
        return cardsLeft.NB_CABLE - nbCableCut;
    }

    public int cutLeftBeforeNewRound() {
        return round.cutLeft();
    }

    public Team getWinner() {
        return winner;
    }

    List<Player> getPlayersByTeam(Team team) {
        LinkedList<Player> result = new LinkedList<>();
        for (Player player : this.players) if (player.getTeam() == team) result.add(player);
        return result;
    }
}
