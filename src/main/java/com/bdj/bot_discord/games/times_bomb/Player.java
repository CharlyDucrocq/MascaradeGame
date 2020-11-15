package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.User;

import java.util.LinkedList;
import java.util.List;

public class Player {
    private User user;
    private LinkedList<Card> hand = new LinkedList<>();
    private Team team = Team.MORIARTY;

    public Player(User user){
        this.user = user;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public List<Card> getBackCardLeft(){
        List<Card> result = hand;
        hand = new LinkedList<>();
        return result;
    }

    public Card cutCard(int targetedCard) {
        return hand.remove(targetedCard);
    }

    public User getUser() {
        return user;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public int nbCardLeft() {
        return hand.size();
    }

    public String getName(){
        return user.getName();
    }

    @Override
    public String toString() {
        return getName()+"("+nbCardLeft()+" cards)";
    }
}