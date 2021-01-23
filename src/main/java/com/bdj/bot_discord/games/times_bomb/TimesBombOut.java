package com.bdj.bot_discord.games.times_bomb;

import com.bdj.bot_discord.discord.utils.QuestionSender;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.*;
import java.util.List;

public class TimesBombOut {
    private enum ColorTheme {
        QUESTION(Color.green),
        START_ROUND(Color.orange),
        START_TURN(Color.DARK_GRAY),
        ;

        final Color color;
        ColorTheme(Color color) {
            this.color = color;
        }
    }

    private Random random = new Random();
    private MessageChannel channel;
    private Map<Player, Message> handMsgMap;

    public TimesBombOut(MessageChannel channel){
        this.channel = channel;
    }

    public void printStart(TimesBombGame game) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("############### **Début de la partie** ###############");
        bd.setColor(ColorTheme.START_ROUND.color);
        bd.addField("Nombre minimum de "+Team.MORIARTY, String.valueOf(game.minBad),true);
        bd.addField("Nombre maximum de "+Team.MORIARTY, String.valueOf(game.maxBad),true);
        channel.sendMessage(bd.build()).queue();

        List<Player> players = game.getPlayers();
        for (Player player : players){
            bd = new EmbedBuilder();
            bd.setTitle("Annonce des rôles :");
            bd.setDescription("Vous faites partie des "+player.getTeam());
            bd.setThumbnail(player.getTeam().getIconUrl());
            player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(bd.build()).queue();
        }
    }

    public void printNewRound(Round round, List<Player> players) {
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Redistribution des cartes");
        bd.setColor(ColorTheme.START_ROUND.color);
        //bd.setThumbnail(Icon.DISTRIBUTION.getIconUrl());
        bd.addField("Cartes par main", String.valueOf(round.CARD_BY_HAND),true);
        bd.addField("Nombre de redistribution avant le game over", String.valueOf(round.roundLeftAfterHim()),true);
        channel.sendMessage(bd.build()).queue();

        handMsgMap = new HashMap<>();
        for(Player player : players) {
            handMsgMap.put(player, player.getUser().getDiscordUser().openPrivateChannel().complete().sendMessage(getHandEmbed(player)).complete());
        }
    }

    private MessageEmbed getHandEmbed(Player player){
        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("Vos cartes :");
        bd.addField("Nombre de Cartes neutres :", String.valueOf(player.getNbCardByType(Card.FAKE)),true);
        bd.addField("Nombre de Cartes câble :", String.valueOf(player.getNbCardByType(Card.CABLE)),true);
        bd.addField("Nombre de Cartes bombe :", String.valueOf(player.getNbCardByType(Card.BOMB)),true);
        return bd.build();
    }

    private void upHandMsgs(Player p){
        Message msg = handMsgMap.get(p);
        msg.editMessage(getHandEmbed(p)).queue();
    }


    public void turnStart(TimesBombGame game) {
        Player currentPlayer = game.getCurrentPlayer();
        EmbedBuilder bd = new EmbedBuilder();
        bd.setColor(ColorTheme.START_TURN.color);
        bd.setTitle("Au tour de "+currentPlayer.getName());
        bd.setThumbnail(currentPlayer.getUser().getDiscordUser().getAvatarUrl());
        bd.addField("Nombre de câbles restant", String.valueOf(game.nbCableLeft()),true);
        bd.addField("Carte à couper avant de redistribuer", String.valueOf(game.cutLeftBeforeNewRound()),true);
        channel.sendMessage(bd.build()).queue();
    }

    public void askForTarget(TimesBombGame game) {
        QuestionSender sender = new QuestionSender(new ArraysChoice<Player>(
                game.getCurrentPlayer().getName()+" : Chez qui souhaitez-vous couper une carte ?",
                game.getCuteablePlayer(),
                target -> {
                    game.cutCard(target, random.nextInt(target.nbCardLeft()));
                }
        ));
        sender.setTarget(game.getCurrentPlayer().getUser().getDiscordUser());
        sender.disableEndMsg();
        sender.setColor(ColorTheme.QUESTION.color);
        sender.send(channel);
    }

    public void printCut(Player currentPlayer, Player target, Card card) {
        upHandMsgs(target);

        EmbedBuilder bd = new EmbedBuilder();
        bd.setAuthor(currentPlayer.getName(), null, currentPlayer.getUser().getDiscordUser().getAvatarUrl());
        bd.setTitle(card.getResultMsg());
        bd.setColor(card.getColor());
        bd.setThumbnail(card.getIconUrl());
        bd.setDescription(currentPlayer.toString()+" a coupé chez "+target);

        channel.sendMessage(bd.build()).queue();
    }

    public void printEndGame(TimesBombGame game) {
        Team winner = game.getWinner();

        EmbedBuilder bd = new EmbedBuilder();
        bd.setTitle("VICTOIRE DES "+winner.name().toUpperCase());
        if(winner == Team.MORIARTY ) bd.setDescription("La bombe a explosée");
        else bd.setDescription("La bombe a été désamorcée");
        bd.setColor(winner.getColor());
        bd.setThumbnail(winner.getIconUrl());

        for (Player player : game.getPlayersByTeam(winner)) bd.addField("",player.getUser().toString(), false);
        channel.sendMessage(bd.build()).queue();
    }

    private MessageChannel saveChannel = new MessageChannel() {
        @Override
        public long getLatestMessageIdLong() {
            return 0;
        }

        @Override
        public boolean hasLatestMessage() {
            return false;
        }

        @Nonnull
        @Override
        public String getName() {
            return null;
        }

        @Nonnull
        @Override
        public ChannelType getType() {
            return null;
        }

        @Nonnull
        @Override
        public JDA getJDA() {
            return null;
        }

        @Override
        public long getIdLong() {
            return 0;
        }
    };
    void mute(){
        MessageChannel c = this.saveChannel;
        this.saveChannel=this.channel;
        this.channel=c;
    }
    void printKill(){
        this.channel.sendMessage("game killed").queue();
    }
}
