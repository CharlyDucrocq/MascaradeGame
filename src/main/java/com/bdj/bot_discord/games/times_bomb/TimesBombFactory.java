package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.lobby.GameFactory;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

public class TimesBombFactory implements GameFactory<TimesBombGame> {
    public final static int MAX_PLAYER = 8;
    public final static int MIN_PLAYER = 4;
    private Player[] players;
    private Random random = new Random();

    private TimesBombOut out;

    public void setPlayers(Collection<User> users){
        LinkedList<User> copy = new LinkedList<>(users);
        Player[] tab = new Player[users.size()];
        int i = 0;
        while (!copy.isEmpty()) tab[i++] = new Player(copy.remove(random.nextInt(copy.size())));
        this.players = tab;
    }

    @Override
    public boolean haveEnoughPlayer(){
        return players.length>= MIN_PLAYER;
    }


    @Override
    public void setDiscordOut(MessageChannel channel){
        this.out = new TimesBombOut(channel);
    }

    @Override
    public TimesBombGame createGame() {
        int minBad;
        int maxBad;
        switch (players.length){
            case 4:{
                minBad = 1;
                maxBad = 2;
                break;
            }
            case 5:{
                minBad = 2;
                maxBad = 2;
                break;
            }
            case 6:{
                minBad = 2;
                maxBad = 2;
                break;
            }
            case 7:{
                minBad = 2;
                maxBad = 3;
                break;
            }
            case 8:{
                minBad = 3;
                maxBad = 3;
                break;
            }
            default: throw new GameException("Mauvais nombre de joueur");
        }
        TimesBombGame game = new TimesBombGame(players,minBad, maxBad);
        game.setOut(out);
        return game;
    }

    @Override
    public int getMinPlayer() {
        return MIN_PLAYER;
    }
}
