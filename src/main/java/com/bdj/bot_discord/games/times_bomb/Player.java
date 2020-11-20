package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.utils.User;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Player {
    private User user;
    private LinkedList<Card> hand = new LinkedList<>();
    private Team team = Team.SHERLOCK;
    int id = 0;

    public Player(User user){
        this.user = user;
    }

    public Player(User user, int id){ //for test
        this(user);
        this.id = id;
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

    public int getNbCardByType(Card type) {
        int count =0;
        for (Card card : hand){
            if(card == type){
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        Player player = (Player) o;
        return Objects.equals(user, player.user) &&
                Objects.equals(hand, player.hand) &&
                team == player.team;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, team, id);
    }
}
