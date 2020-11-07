package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.ActionNotAllowed;
import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.CardCreator;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;

public class GameRound {
    private final Game game;
    MascaradeOut out;

    Player player;
    List<Player> contestPlayers = new LinkedList<>();
    CardCreator charaChose;
    Instant characterChoiceInstant;

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
        if(!game.getCharactersList().contains(c))
            throw new GameException("Le personnage n'est pas dans la partie");
        characterChoiceInstant = Instant.now();
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
        out.printSwitch(player, otherPlayer, really);
        endTurn();
    }

    public void useCharacter(){
        useCharacterOnlyForTest(charaChose); // separated for test
    }

    void useCharacterOnlyForTest(CardCreator creator){
        if (charaChose==null)
            throw new GameException("Le personnage doit être definie avant !");

        long timeLeft = GlobalParameter.CHOICE_USE_TIME_IN_SEC-Duration.between(characterChoiceInstant,Instant.now()).getSeconds();
        if(timeLeft>0) throw new GameException("Il reste "+timeLeft+" avant de pouvoir utilisé l'action");

        if(noContestPlayers()){
            Card card = creator.getCard(player,game);
            card.action();
            out.printAction(player,card);
            endTurn();
            return;
        }

        contestPlayers.add(0, player);

        // sort to place the one who have the character in the first place
        contestPlayers.sort(Comparator.comparing(
                p -> player.getCurrentCharacter() == charaChose ? 0:1));

        for (Player p : contestPlayers) {
            if (p.getCurrentCharacter() == charaChose) {
                Card card = creator.getCard(p,game);
                card.action();
                out.printAction(p,card);
            } else {
                p.payPenalty(game.getBank());
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

    public boolean noContestPlayers(){
        return contestPlayers.isEmpty();
    }

    public int howManyIn(Character peasant) {
        int count=0;
        for (Player player : contestPlayers){
            if(player.getCurrentCharacter()==peasant) count++;
        }
        return count;
    }
}
