package com.bdj.bot_discord.games.times_bomb;

import java.util.LinkedList;
import java.util.Random;

import static com.bdj.bot_discord.games.times_bomb.Card.*;

public class Deck extends LinkedList<Card> {
    public static final int NB_STARTED_CARD_BY_PLAYER = 5;

    public final int NB_CABLE;
    public final int NB_FAKE;
    public final int NB_BOMB = 1;

    public final Random random = new Random();

    Deck(int nbPlayer){
        NB_CABLE = nbPlayer;
        NB_FAKE = (nbPlayer*4)-1;

        for (int i=0;i<NB_CABLE;i++) this.add(CABLE);
        for (int i=0;i<NB_FAKE;i++) this.add(FAKE);
        for (int i=0;i<NB_BOMB;i++) this.add(BOMB);
    }

    public Card removeRand(){
        return this.remove(random.nextInt(this.size()));
    }
}
