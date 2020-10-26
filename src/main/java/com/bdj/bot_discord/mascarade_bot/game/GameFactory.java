package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.game.card.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.bdj.bot_discord.mascarade_bot.game.card.Character.*;

public class GameFactory {
    private Player[] players;
    private Random random = new Random();


    public GameFactory(List<Player> players) {
        Player[] tab = new Player[players.size()];
        int i =0;
        for(Player p : players) tab[i++] = p;
        this.players = tab;
    }

    public Game createGame(){
        List<Character> characters = new LinkedList<>();
        characters.add(JUDGE);
        switch (players.length){
            case 7 : {
                characters.add(BISHOP);
                characters.add(KING);
                characters.add(QUEEN);
                characters.add(WITCH);
                characters.add(BEGGAR);
                characters.add(THIEF);
            }
            default: {
                if(characters.size()==1) return null;
                for(Player p : players){
                    p.setCurrentCharacter(characters.remove(random.nextInt(characters.size())));
                }
                break;
            }
        }
        return new Game(players);
    }
}
