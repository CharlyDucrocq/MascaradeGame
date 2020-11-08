package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.CountDown;
import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.ActionNotAllowed;
import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import com.bdj.bot_discord.mascarade_bot.errors.QuestionInProgress;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.CardCreator;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.choice.YesOrNoQuestion;
import com.bdj.bot_discord.mascarade_bot.utils.choice.user.UserQuestion;

import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class GameRound {
    private final Game game;
    MascaradeOut out;

    Player player;
    List<Player> contestPlayers = new LinkedList<>();
    CardCreator charaChose;
    Instant characterChoiceInstant;

    QuestionToPlayer questionInProgress = new QuestionToPlayer();

    boolean contestAllowed = true;
    boolean isEnded = false;

    GameRound(Game game, Player player){
        this.game = game;
        this.player = player;
        this.out = game.getOut();
        out.printStartTurn(this);
    }

    public void contest(Player opponent){
        if(!contestAllowed) throw new ActionNotAllowed("Vous n'êtes pas autorisé à faire ça maintenant");
        if (contestPlayers.remove(opponent))
            out.printUncontest(opponent);
        else {
            contestPlayers.add(opponent);
            out.printContest(opponent);
        }
    }

    public void setCharacterToUse(Character c){
        if(!questionInProgress.noQuestionInProgress()) throw new QuestionInProgress();
        if(!game.getCharactersList().contains(c))
            throw new GameException("Le personnage n'est pas dans la partie");
        characterChoiceInstant = Instant.now();
        charaChose = c;
        out.printSetCharacter(this);
    }

    public void peekCharacter(){
        if(!questionInProgress.noQuestionInProgress()) throw new QuestionInProgress();
        out.printPeek(player);
        endTurn();
    }

    public void askForSwitch(){
        if(!questionInProgress.noQuestionInProgress()) throw new QuestionInProgress();
        GameRound round = this;
        UserQuestion userChoice = new UserQuestion(
                "Avec qui voulez-vous échanger (ou pas) ?",
                game.getUsers() ,
                otherUser ->{
                    Player otherPlayer = game.getPlayer(otherUser);
                    YesOrNoQuestion yesOrNo = new YesOrNoQuestion(
                            "Voulez-vous réellement échanger les cartes ?",
                            () -> round.switchCard(otherPlayer, true),
                            () -> round.switchCard(otherPlayer, false));
                    questionInProgress.set(yesOrNo,player, QuestionToPlayer.Type.SWITCH_YES_NO).send();
                });
        questionInProgress.set(userChoice,player, QuestionToPlayer.Type.SWITCH_PLAYER).send();
    }

    public void switchCard(Player otherPlayer, boolean really){
        if(questionInProgress.getType() == QuestionToPlayer.Type.SWITCH_YES_NO) questionInProgress.clear();
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
        if(!questionInProgress.noQuestionInProgress()) throw new QuestionInProgress();
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
