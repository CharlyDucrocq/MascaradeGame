package game;

import discord.User;
import game.card.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameRound extends Observable {
    Player player;
    List<Player> contestPlayers = new LinkedList<>();
    Character charaChose;

    boolean isEnded = false;

    GameRound(Player player){

    }

    public boolean contest(Player opponent){
        if (contestPlayers.contains(opponent)) return false;
        contestPlayers.add(opponent);
        return true;
    }

    public void setCharacterToUse(Character c){
        charaChose = c;
    }

    public Character peekCharacter(){
        return player.getCurrentCharacter();
    }

    public void switchCard(Player otherPlayer, boolean really){
        if(really){
            Character c = player.getCurrentCharacter();
            player.setCurrentCharacter(otherPlayer.getCurrentCharacter());
            otherPlayer.setCurrentCharacter(c);
        }
        endTurn();
    }

    public void useCharacter(){
        contestPlayers.add(0, player);
        for (Player p : contestPlayers) {
            if (p.getCurrentCharacter() == charaChose) {
                //TODO action
            } else {
                //TODO penality
            }
        }
        contestPlayers.remove(player);
        endTurn();
    }

    private void endTurn(){
        isEnded = true;
        notifyObservers();
    }

    public boolean isEnded() {
        return isEnded = true;
    }

    public User getUser() {
        return player.getUser();
    }
}
