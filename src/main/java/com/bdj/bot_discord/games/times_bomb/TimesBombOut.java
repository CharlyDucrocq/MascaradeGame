package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.QuestionSender;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class TimesBombOut {
    private Random random = new Random();
    private MessageChannel channel;

    public TimesBombOut(MessageChannel channel){
        this.channel = channel;
    }

    public void printStart(TimesBombGame game) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("############### **Début de la partie** ###############");
        bd.addField("Nombre minimum de "+Team.MORIARTY, String.valueOf(game.minBad),true);
        bd.addField("Nombre maximum de "+Team.MORIARTY, String.valueOf(game.maxBad),true);
        channel.sendMessage(bd.build()).queue();
    }

    public void printNewRound(Round round) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Redistribution des cartes");
        //bd.setThumbnail(Icon.DISTRIBUTION.getIconUrl());
        bd.addField("Cartes par main", String.valueOf(round.CARD_BY_HAND),true);
        bd.addField("Nombre de redistribution avant le game over", String.valueOf(round.roundLeftAfterHim()),true);
        channel.sendMessage(bd.build()).queue();
    }


    public void turnStart(TimesBombGame game) {
        Player currentPlayer = game.getCurrentPlayer();
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Au tour de "+currentPlayer.getName());
        bd.setThumbnail(currentPlayer.getUser().getDiscordUser().getAvatarUrl());
        bd.addField("Nombre de cables restant", String.valueOf(game.nbCableLeft()),true);
        bd.addField("Carte à coupé avant de redistribuer", String.valueOf(game.cutLeftBeforeNewRound()),true);
        channel.sendMessage(bd.build()).queue();
    }

    public void askForTarget(TimesBombGame game) {
        QuestionSender sender = new QuestionSender(new ArraysChoice<Player>(
                game.getCurrentPlayer().getName()+" : Chez qui souhaite-tu couper une carte ?",
                game.getCuteablePlayer(),
                target -> {
                    game.cutCard(target, random.nextInt(target.nbCardLeft()));
                }
        ));
        sender.setTarget(game.getCurrentPlayer().getUser().getDiscordUser());
        sender.disableEndMsg();
        sender.send(channel);
    }

    public void printCut(Player currentPlayer, Player target, Card card) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(currentPlayer.getName(), null, currentPlayer.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(card.getResultMsg());
        bd.setThumbnail(card.getIconUrl());
        bd.setDescription(currentPlayer.toString()+" a coupé chez "+target);

        channel.sendMessage(bd.build()).queue();
    }

    public void printEndGame(TimesBombGame game) {
        Team winner = game.getWinner();

        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("VICTOIRE DES "+winner.name().toUpperCase());
        if(winner == Team.MORIARTY ) bd.setDescription("La bombe a explosé");
        else bd.setDescription("La bombe a été désamorcé");
        bd.setThumbnail(winner.getIconUrl());

        for (Player player : game.getPlayers(winner)) bd.addField(player.getName(),"", false);
    }
}
