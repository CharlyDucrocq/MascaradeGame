package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.errors.ActionNotAllowed;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.games.mascarade.card.Card;
import com.bdj.bot_discord.games.mascarade.card.CardCreator;
import com.bdj.bot_discord.games.mascarade.card.Character;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GameRound {
    private final MascaradeGame game;
    private final MascaradeOut out;

    public final Player player;
    public final List<Player> contestPlayers = new LinkedList<>();
    Character charaChose;
    Instant characterChoiceInstant;

    private boolean contestAllowed = true;
    private boolean isEnded = false;

    protected RoundAction[] actionsAvailable = new RoundAction[]{RoundAction.PEEK, RoundAction.SWITCH, RoundAction.USE};

    GameRound(MascaradeGame game, Player player){
        this.game = game;
        this.player = player;
        this.out = game.getOut();
        out.printStartTurn(this);
        out.askToChooseAction(this);
    }

    public RoundAction[] getActionsAvailable() {
        return actionsAvailable;
    }

    public synchronized void contest(Player opponent){
        if(!contestAllowed) throw new ActionNotAllowed("Vous n'êtes pas autorisé à faire ça maintenant");
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

    public void askForSwitch(){
        out.askForSwitch(this);
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

    public void askForUse() {
        out.askForUse(this);
    }

    public void useCharacter(){
        new Thread(this::useCharacterWithExceptionCatcher).start(); // separated for test
    }

    private void useCharacterWithExceptionCatcher(){
        try {
            useCharacterOnlyForTest(charaChose);
        } catch (GameException e){
            out.printError(e);
        } catch (Exception e){
            e.printStackTrace();
        }
        if(!isEnded) endTurn();
    }

    synchronized void useCharacterOnlyForTest(CardCreator creator){
        if (charaChose==null)
            throw new GameException("Le personnage doit être definie avant !");

        long timeLeft = GlobalParameter.CHOICE_USE_TIME_IN_SEC-Duration.between(characterChoiceInstant,Instant.now()).getSeconds();
        if(timeLeft>0) throw new GameException("Il reste "+timeLeft+" avant de pouvoir utilisé l'action");

        contestAllowed=false;
        if(noContestPlayers()){
            Card card = creator.getCard(player,game);
            card.action();
            out.printAction(player,card);
            endTurn();
            return;
        }

        contestPlayers.remove(player);
        contestPlayers.add(0, player);

        // sort to place the one who have the character in the first place
        if(charaChose == Character.SPY || charaChose == Character.JOKER)
            contestPlayers.sort(Comparator.comparing(
                p -> player.getCurrentCharacter() == charaChose ? 1:0));
        else  contestPlayers.sort(Comparator.comparing(
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

    public MascaradeGame getGame() {
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
