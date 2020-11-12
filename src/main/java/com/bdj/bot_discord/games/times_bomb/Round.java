package com.bdj.bot_discord.games.times_bomb;

import java.util.LinkedList;

public class Round {
    public final static int CARD_BY_HAND_LIMIT = 2;

    public final int LIMIT;
    public final int CARD_BY_HAND;
    private final LinkedList<Turn> turns = new LinkedList<>();

    Round(LinkedList<Player> players, Deck cardsLeft, TimesBombOut out) {
        LIMIT = players.size();
        int count =0;
        while (!cardsLeft.isEmpty()){
            for (Player player :players) player.addCard(cardsLeft.removeRand());
            count++;
        }
        CARD_BY_HAND = count;
        out.printNewRound(this);
    }

    void newTurn(Player player, Player target, Card card){
        turns.add(new Turn(player, target, card));
    }

    boolean over(){
        return turns.size() >= LIMIT;
    }

    public int cutLeft() {
        return LIMIT-turns.size();
    }

    public int roundLeftAfterHim() {
        return CARD_BY_HAND_LIMIT - CARD_BY_HAND;
    }

    public boolean isTheLast(){
        return this.roundLeftAfterHim()==0;
    }

    private static class Turn{
        public final Player player;
        public final Player target;
        public final Card cardCut;

        Turn(Player p, Player t, Card c){
            player = p;
            target = t;
            cardCut = c;
        }
    }
}
