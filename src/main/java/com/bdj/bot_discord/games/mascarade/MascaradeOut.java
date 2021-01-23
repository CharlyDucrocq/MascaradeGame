package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.discord.utils.*;
import com.bdj.bot_discord.games.mascarade.card.Card;
import com.bdj.bot_discord.games.mascarade.card.Character;
import com.bdj.bot_discord.games.mascarade.card.Inquisitor;
import com.bdj.bot_discord.main.Application;
import com.bdj.bot_discord.utils.MyFuture;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.bdj.bot_discord.utils.choice.YesOrNoQuestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MascaradeOut {
    private enum ColorTheme {
        QUESTION(Color.green),
        START_ROUND(Color.orange),
        INFO(new Color(50,50,200)),
        ACTION(new Color(120,90,150)),
        USE_CHARACTER(new Color(200,250,150)),
        PENALTY(new Color(220,50,40)),
        ;

        final Color color;
        ColorTheme(Color color) {
            this.color = color;
        }
    }

    private MessageChannel channel;

    private QuestionSender lastQuestion;
    public void reRunLastQuestion() {
        if(lastQuestion.isDone()) throw new RuntimeException("Question done");
        lastQuestion.sendAgain();
    }

    public MascaradeOut(MessageChannel channel){
        this.channel = channel;
    }

    public void printStart(MascaradeGame game){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("############### **Début de la partie** ###############");
        bd.setColor(ColorTheme.START_ROUND.color);
        channel.sendMessage(bd.build()).queue();

        printCharactersRecap(game.getCharactersList());

        bd = new EmbedBuilder();
        bd.setTitle("Configuration de départ des Joueurs :\n");
        bd.setColor(ColorTheme.INFO.color);
        for (Player player : game.getTable().getPlayers()){
            bd.addField(player.toString(),player.getCurrentCharacter().toString(),true);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printCharactersRecap(List<Character> charactersList) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Les Personnages", null);

        eb.setColor(ColorTheme.INFO.color);

        for (Character character : charactersList) eb.addField(character.toString(), character.getDescription(), false);

        channel.sendMessage(eb.build()).queue();
    }

    public void printPlayerRecap(Player[] players){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Situation actuel :");
        bd.setColor(ColorTheme.INFO.color);
        for (Player player : players){
            bd.addField(" - "+player.toString(),"",true);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printPeek(Player player){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle("Action de tour");
        bd.setColor(ColorTheme.ACTION.color);
        bd.setThumbnail(RoundAction.PEEK.getIconUrl());
        bd.setDescription(player.toString()+" regarde son role.");
        channel.sendMessage(bd.build()).queue();

        bd = new EmbedBuilder();
        bd.setTitle("Vous êtes " +player.getCurrentCharacter().toString());
        bd.setColor(ColorTheme.ACTION.color);
        bd.setDescription(player.getCurrentCharacter().getDescription());
        bd.setThumbnail(player.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();
    }

    public void printStartTurn(GameRound gameRound) {
        Player player = gameRound.player;
        EmbedBuilder bd = new EmbedBuilder();
        bd.setColor(ColorTheme.START_ROUND.color);
        bd.setTitle("Nouveau tour");
        bd.setThumbnail(player.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription("C'est au tour de "+gameRound.player.toString());
        channel.sendMessage(bd.build()).queue();
    }

    public RoundAction askToChooseAction(GameRound round) {
        MyFuture<RoundAction> answer = new MyFuture<>();
        QuestionSender sender = new QuestionSender(new ArraysChoice<>(
                "Quelle action souhaitez-vous réaliser ?",
                round.getActionsAvailable(),
                answer::sendAnswer));
        sender.disableEndMsg();
        lastQuestion = sender;
        sender.setTarget(round.player.getUser().getDiscordUser());
        sender.setColor(ColorTheme.QUESTION.color);
        sender.send(channel);
        return answer.get();
    }

    public void printSwitch(Player player, Player other, boolean trueOrNot){
        if(trueOrNot)
            player.getUser().sendMessage("Vous avez vraiment fait l'échange avec "+other.toString());
        else
            player.getUser().sendMessage("Vous n'avez pas fait l'échange avec "+other.toString());


        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle("Action de tour");
        bd.setThumbnail(RoundAction.SWITCH.getIconUrl());
        bd.setColor(ColorTheme.ACTION.color);
        bd.setDescription(player.toString()+" échange (ou pas) ses cartes avec "+other.toString());
        channel.sendMessage(bd.build()).queue();
    }

    public void printContest(Player opponent) {
        channel.sendMessage(opponent.toString()+" s'oppose !").queue();
    }

    public void printUncontest(Player opponent) {
        channel.sendMessage(opponent.toString()+" ne s'oppose plus !").queue();
    }

    public void askForUse(GameRound round) {
        MascaradeGame game = round.getGame();
        net.dv8tion.jda.api.entities.User user = round.player.getUser().getDiscordUser();
        ArraysChoice<Character> charChoice = new ArraysChoice<>(
                "Quelle personnage affirmez-vous être ?",
                game.getCharactersList() ,
                round::setCharacterToUse);
        QuestionSender sender = new QuestionSender(charChoice);
        sender.disableEndMsg();
        lastQuestion = sender;
        sender.setTarget(user);
        sender.setColor(ColorTheme.QUESTION.color);
        sender.send(channel);
    }

    private CountDown countDown;
    private ReactionAnalyser contestWaiter;

    public void printSetCharacter(GameRound gameRound) {
        Player player = gameRound.player;

        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle("Action de tour");
        bd.setThumbnail(gameRound.charaChose.getIconUrl());
        bd.setColor(ColorTheme.ACTION.color);
        bd.setDescription(gameRound.player.toString()+" affirme être "+gameRound.charaChose.toString());
        bd.setFooter("Vous pouvez contester avec "+MyEmote.OBJECTION.getId());
        Message msg = channel.sendMessage(bd.build()).complete();

        contestWaiter = new ReactionAnalyser(msg);
        contestWaiter.addReaction(MyEmote.OBJECTION,
                e-> gameRound.contest(gameRound.getGame().getPlayer(Application.userList.getUser(new User(e.getUser())))),
                e-> gameRound.contest(gameRound.getGame().getPlayer(Application.userList.getUser(new User(e.getUser())))));
    }

    public void waitForContest(GameRound gameRound) {
        MyFuture<Boolean> waiter = new MyFuture<>();
        if (countDown != null) countDown.kill();
        countDown = new CountDown(GlobalParameter.CHOICE_USE_TIME_IN_SEC, channel,
                "Il reste ", "s avant de pouvoir utiliser le pouvoir",
                "",
                ()-> {
                    contestWaiter.killAll();
                    countDown=null;
                    waiter.sendAnswer(true);
                });
        waiter.get();
    }

    public void printAction(Player player, Card charaChose) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(player.toString()+" a utilisé le pouvoir "+charaChose.getType().toString());
        bd.setThumbnail(charaChose.getType().getIconUrl());
        bd.setDescription(charaChose.getDescription());
        bd.setColor(ColorTheme.USE_CHARACTER.color);
        channel.sendMessage(bd.build()).queue();
    }

    public void printPenality(Player player) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(player.toString()+" : perdu, vous étiez "+player.getCurrentCharacter().toString()+"... ");
        bd.setColor(ColorTheme.PENALTY.color);
        bd.setThumbnail(RoundAction.penalityIconUrl);
        bd.setDescription("Vous avez payé "+GlobalParameter.PENALTY+" piece(s) d'or à la banque.");
        channel.sendMessage(bd.build()).queue();
    }

    public void printEnd(MascaradeGame game) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("############### Fin de la partie ###############\n" +
                    "################################################");
        bd.setColor(ColorTheme.START_ROUND.color);
        channel.sendMessage(bd.build()).queue();
    }

    public void printPodium(TableRound tableRound) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("#################### Poduim ####################**");
        int i=1;
        List<Player> players = Arrays.asList(tableRound.getPlayers());
        players.sort(Comparator.comparing(p->(-p.getPurse().getValue())));
        for (Player player : players) {
            bd.addField(i++ +"  -  "+player.toString(),"",false);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printSpy(Player player, Player target) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Vous êtes " +player.getCurrentCharacter().toString());
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription(player.getCurrentCharacter().getDescription());
        bd.setColor(ColorTheme.INFO.color);
        bd.setThumbnail(player.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();

        bd = new EmbedBuilder();
        bd.setTitle(target.toString()+" est " +target.getCurrentCharacter().toString());
        bd.setAuthor(target.toString(),null,target.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription(target.getCurrentCharacter().getDescription());
        bd.setColor(ColorTheme.INFO.color);
        bd.setThumbnail(target.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();
    }

    public Character askForAChar(Player player, List<Character> chars, String question, boolean inPrivate) {
        MyFuture<Character> answer = new MyFuture<>();
        QuestionSender sender1 = new QuestionSender(new ArraysChoice<>(
                player.toString()+" : "+question,
                chars,
                answer::sendAnswer
        ));
        lastQuestion = sender1;
        sender1.setTarget(player.getUser().getDiscordUser());
        sender1.disableEndMsg();
        sender1.setColor(ColorTheme.QUESTION.color);
        if (inPrivate) sender1.send(player.getUser().getDiscordUser().openPrivateChannel().complete());
        else sender1.send(this.channel);
        return answer.get();
    }

    public Player askForAPlayer(Player player, List<Player> players, String question, boolean inPrivate) {
        MyFuture<Player> answer = new MyFuture<>();
        QuestionSender sender1 = new QuestionSender(new ArraysChoice<>(
                player.toString()+" : "+question,
                players,
                answer::sendAnswer
        ));
        lastQuestion = sender1;
        sender1.setTarget(player.getUser().getDiscordUser());
        sender1.disableEndMsg();
        sender1.setColor(ColorTheme.QUESTION.color);
        if (inPrivate) sender1.send(player.getUser().getDiscordUser().openPrivateChannel().complete());
        else sender1.send(this.channel);
        return answer.get();
    }

    public boolean askForABoolean(Player player, String question, boolean inPrivate) {
        MyFuture<Boolean> answer = new MyFuture<>();
        QuestionSender sender1 = new QuestionSender(new YesOrNoQuestion(
                player.toString()+" : "+question,
                ()->answer.sendAnswer(true),
                ()->answer.sendAnswer(false)
        ));
        lastQuestion = sender1;
        sender1.setTarget(player.getUser().getDiscordUser());
        sender1.disableEndMsg();
        sender1.setColor(ColorTheme.QUESTION.color);
        if (inPrivate) sender1.send(player.getUser().getDiscordUser().openPrivateChannel().complete());
        else sender1.send(this.channel);
        return answer.get();
    }

    public void printError(Exception e) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Erreur", null);
        eb.setDescription(e.getMessage());
        eb.setColor(Color.red);

        channel.sendMessage(eb.build()).queue();
    }
}
