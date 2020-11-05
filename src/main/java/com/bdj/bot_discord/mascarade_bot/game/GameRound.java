package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameRound extends Observable {
    MascaradeOut out;

    Player player;
    List<Player> contestPlayers = new LinkedList<>();
    Character charaChose;

    boolean isEnded = false;

    GameRound(MascaradeOut out, Player player){
        this.player = player;
        this.out = out;
        out.printStartTurn(this);
    }

    public void contest(Player opponent){
        if (contestPlayers.remove(opponent))
            out.printUncontest(opponent);
        else {
            contestPlayers.add(opponent);
            out.printContest(opponent);
        }
    }

    public void setCharacterToUse(Character c){
        charaChose = c;
        out.printSetCharacter(this);
    }

    public void peekCharacter(){
        out.printPeek(player);
        endTurn();
    }

    public void switchCard(Player otherPlayer, boolean really){
        if(really){
            Character c = player.getCurrentCharacter();
            player.setCurrentCharacter(otherPlayer.getCurrentCharacter());
            otherPlayer.setCurrentCharacter(c);
        }
        out.printPublicSwitch(player, otherPlayer);
        endTurn();
    }

    public void useCharacter(){
        contestPlayers.add(0, player);
        for (Player p : contestPlayers) {
            if (p.getCurrentCharacter() == charaChose) {
                //TODO action
                out.printAction(p,charaChose);
            } else {
                //TODO penality
                out.printPenality(p);
            }
        }
        contestPlayers.remove(player);
        endTurn();
    }

    private void endTurn(){
        isEnded = true;
        setChanged();
        notifyObservers();
    }

    public boolean isEnded() {
        return isEnded;
    }

    public User getUser() {
        return player.getUser();
    }
}
