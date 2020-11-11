package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.discord.*;
import com.bdj.bot_discord.discord.commands.mascarade.game.UseAction;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.game.card.Inquisitor;
import com.bdj.bot_discord.mascarade_bot.main.Application;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.choice.YesOrNoQuestion;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MascaradeOut {
    private MessageChannel channel;

    public MascaradeOut(MessageChannel channel){
        this.channel = channel;
    }

    public void printStart(MascaradeGame game){
        channel.sendMessage(
                "############### **Début de la partie** ###############\n"
        ).queue();
        printCharactersRecap(game.getCharactersList());

        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Configuration de départ des Joueurs :\n");
        for (Player player : game.getTable().getPlayers()){
            bd.addField(player.toString(),player.getCurrentCharacter().toString(),false);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printCharactersRecap(List<Character> charactersList) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Les Personnages", null);

        eb.setColor(ColorTheme.INFO.getColor());

        for (Character character : charactersList) eb.addField(character.toString(), character.getDescription(), false);

        channel.sendMessage(eb.build()).queue();
    }

    public void printPlayerRecap(Player[] players){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("**Situation actuel :**");
        for (Player player : players){
            bd.addField(" - "+player.toString(),"",false);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printPeek(Player player){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle("Action de tour");
        bd.setThumbnail(RoundAction.PEEK.getIconUrl());
        bd.setDescription(player.toString()+" regarde son role.");
        channel.sendMessage(bd.build()).queue();

        bd = new EmbedBuilder();
        bd.setTitle("Vous êtes " +player.getCurrentCharacter().toString());
        bd.setDescription(player.getCurrentCharacter().getDescription());
        bd.setThumbnail(player.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();
    }

    public void printStartTurn(GameRound gameRound) {
        Player player = gameRound.player;
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Nouveau tour");
        bd.setThumbnail(player.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription("C'est au tour de "+gameRound.player.toString());
        channel.sendMessage(bd.build()).queue();
    }

    public void askToChooseAction(GameRound round) {
        QuestionAnswers questionAnswers = new ArraysChoice<>(
                "Quelle action souhaitez-vous réaliser ?",
                round.getActionsAvailable(),
                action -> action.doAction(round));

    }

    public void askForSwitch(GameRound round) {
        MascaradeGame game = round.getGame();
        net.dv8tion.jda.api.entities.User user = round.player.getUser().getDiscordUser();
        ArraysChoice<User> userChoice = new ArraysChoice<>(
                "Avec qui voulez-vous échanger (ou pas) ?",
                game.getUsers() ,
                otherUser ->{
                    Player otherPlayer = game.getPlayer(otherUser);
                    YesOrNoQuestion yesOrNo = new YesOrNoQuestion(
                            "Voulez-vous réellement échanger les cartes ?",
                            () -> round.switchCard(otherPlayer, true),
                            () -> round.switchCard(otherPlayer, false));
                    QuestionSender sender = new QuestionSender(yesOrNo);
                    sender.disableEndMsg();
                    sender.setTarget(user);
                    sender.send(user.openPrivateChannel().complete());
                });
        QuestionSender sender = new QuestionSender(userChoice);
        sender.disableEndMsg();
        sender.setTarget(user);
        sender.send(channel);
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
        ArraysChoice<Character> charChoice = new ArraysChoice<Character>(
                "Quelle personnage affirmez-vous être ?",
                game.getCharactersList() ,
                round::setCharacterToUse);
        QuestionSender sender = new QuestionSender(charChoice);
        sender.disableEndMsg();
        sender.setTarget(user);
        sender.send(channel);
    }

    private CountDown countDown;

    public void printSetCharacter(GameRound gameRound) {
        Player player = gameRound.player;

        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle("Action de tour");
        bd.setThumbnail(gameRound.charaChose.getIconUrl());
        bd.setDescription(gameRound.player.toString()+" affirme être "+gameRound.charaChose.toString());
        Message msg = channel.sendMessage(bd.build()).complete();

        ReactionAnalyser analyser = new ReactionAnalyser(msg);
        analyser.addReaction(MyEmote.OBJECTION,
                e->{gameRound.contest(gameRound.getGame().getPlayer(Application.userList.getUser(new User(e.getUser()))));},
                e->{gameRound.contest(gameRound.getGame().getPlayer(Application.userList.getUser(new User(e.getUser()))));});

        if (countDown != null) countDown.kill();
        countDown = new CountDown(GlobalParameter.CHOICE_USE_TIME_IN_SEC, channel,
                "Il reste ", "s avant de pouvoir utiliser le pouvoir",
                "Vous pouvez utiliser le pouvoir avec !"+ new UseAction().getName(),
                ()-> {
                    analyser.killAll();
                    gameRound.askForUse();
                });
    }


    public void printAction(Player player, Card charaChose) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(player.toString()+" a utilisé le pouvoir "+charaChose.getType().toString());
        bd.setThumbnail(charaChose.getType().getIconUrl());
        bd.setDescription(charaChose.getDescription());
        channel.sendMessage(bd.build()).queue();
    }

    public void printPenality(Player player) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(player.toString()+" : perdu, vous étiez "+player.getCurrentCharacter().toString()+"... ");
        bd.setThumbnail(RoundAction.penalityIconUrl);
        bd.setDescription("Vous avez payé "+GlobalParameter.PENALTY+" piece(s) d'or à la banque.");
        channel.sendMessage(bd.build()).queue();
    }

    public void printEnd(MascaradeGame game) {
        channel.sendMessage(
                "############### **Fin de la partie** ###############\n" +
                "##################################################"
        ).queue();
    }

    public void printPodium(TableRound tableRound) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("#################### **Poduim** ####################**");
        int i=1;
        List<Player> players = Arrays.asList(tableRound.getPlayers());
        players.sort(Comparator.comparing(p->(-p.getPurse().getValue())));
        for (Player player : players) {
            bd.addField(String.valueOf(i++),player.toString(),false);
        }
        channel.sendMessage(bd.build()).queue();
    }

    public void printSpy(Player player, Player target) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Vous êtes " +player.getCurrentCharacter().toString());
        bd.setAuthor(player.toString(),null,player.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription(player.getCurrentCharacter().getDescription());
        bd.setThumbnail(player.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();

        bd = new EmbedBuilder();
        bd.setTitle(target.toString()+" est " +target.getCurrentCharacter().toString());
        bd.setAuthor(target.toString(),null,target.getUser().getDiscordUser().getAvatarUrl());
        bd.setDescription(target.getCurrentCharacter().getDescription());
        bd.setThumbnail(target.getCurrentCharacter().getIconUrl());
        player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();
    }

    public void inquisitorProceed(Player player, Inquisitor inquisitor) {
        QuestionSender sender1 = new QuestionSender(new ArraysChoice<>(
                player.toString()+" : En tant qu'inquisiteur, à qui demandez-vous de deviner sa carte ?",
                inquisitor.players,
                target -> {
                    QuestionSender sender2 = new QuestionSender(new ArraysChoice<>(
                            target.toString()+" : Quelle est d'après vous votre carte ?",
                            inquisitor.characters,
                            character -> {
                                inquisitor.processInfo(target, character);
                            }
                    ));
                    sender2.setTarget(target.getUser().getDiscordUser());
                    sender2.disableEndMsg();
                    sender2.send(this.channel);
                }
        ));
        sender1.setTarget(player.getUser().getDiscordUser());
        sender1.disableEndMsg();
        sender1.send(this.channel);
    }
}
