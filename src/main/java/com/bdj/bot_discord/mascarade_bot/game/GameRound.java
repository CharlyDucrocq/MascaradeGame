package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameRound {
    private final Game game;
    MascaradeOut out;

    Player player;
    List<Player> contestPlayers = new LinkedList<>();
    Character charaChose;

    boolean isEnded = false;

    GameRound(Game game, Player player){
        this.game = game;
        this.player = player;
        this.out = game.getOut();
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

        // sort to place the one who have the character in the first place
        contestPlayers.sort(Comparator.comparing(
                p -> Math.abs(player.getCurrentCharacter().ordinal()-charaChose.ordinal())));

        for (Player p : contestPlayers) {
            if (p.getCurrentCharacter() == charaChose) {
                Card card = charaChose.getCard(p,game);
                card.action();
                out.printAction(p,card);
            } else {
                player.payPenalty(game.getBank());
                out.printPenality(p);
            }
        }
        contestPlayers.remove(player);
        endTurn();
    }

    private void endTurn(){
        isEnded = true;
        game.update(this);
    }

    public boolean isEnded() {
        return isEnded;
    }

    public User getUser() {
        return player.getUser();
    }

    public Game getGame() {
        return game;
    }
}
