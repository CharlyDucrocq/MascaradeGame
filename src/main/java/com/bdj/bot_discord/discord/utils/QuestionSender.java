package com.bdj.bot_discord.discord.utils;

import com.bdj.bot_discord.utils.choice.Answer;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static com.bdj.bot_discord.main.Application.waiter;

public class QuestionSender {
    private boolean done = false;
    private QuestionAnswers question;
    private LinkedList<MyEmote> emotes = new LinkedList<>();

    private boolean printChoiceMsg = true;
    private Consumer<Answer> afterAnswerAction = a->{};

    private User target;
    private Message msg;
    private Color color;
    private MessageChannel channel;

    public QuestionSender(QuestionAnswers questionAnswers){
        question = questionAnswers;
        for (MyEmote emote : MyEmote.values()){
            if(emotes.size()<questionAnswers.getAnswers().size()) emotes.add(emote);
        }
    }

    private MessageEmbed getMsgEmbed(){
        EmbedBuilder bd = new EmbedBuilder();

        if(target!=null){
            bd.setAuthor(target.getName(),null,target.getAvatarUrl());
        }

        bd.setTitle(question.getQuestion());
        bd.setDescription("Cliquez sur la réaction associée à votre choix.------------------------------------------------------------->");
        if(color != null) bd.setColor(color);

        int i = 0;
        for (Answer answer : question.getAnswers()) {
            bd.addField(emotes.get(i++).getId()+" : "+answer.getDescription(),"", true);
        }

        return bd.build();
    }

    public void setTarget(User user){
        this.target = user;
    }

    public void disableEndMsg(){
        this.printChoiceMsg = false;
    }

    public Message send(MessageChannel channel){
        this.channel = channel;
        msg = channel.sendMessage(getMsgEmbed()).complete();
        Message msg = this.msg;
        User target = this.target;
        waiter.waitForEvent(MessageReactionAddEvent.class, (e)->haveToReact(e, msg, target, emotes), this::react);
        try{
            for (MyEmote emote : emotes) msg.addReaction(emote.getId()).complete();
        } catch (ErrorResponseException ignored){

        }
        return msg;
    }

    public void sendAgain() {
        msg.delete().queue();
        channel.sendMessage("test").queue();
        send(channel);
    }

    public boolean isDone(){
        return this.done;
    }

    private static boolean haveToReact(MessageReactionAddEvent event, Message msg, User target, List<MyEmote> emotes){
        User author = event.getUser();
        if (author != null && author.isBot()) return false;
        String msgId = event.getMessageId();
        return (author != null && author.equals(target))
                && (msg!=null && msgId.equals(msg.getId()))
                && emotes.contains(MyEmote.getFromId(event.getReactionEmote().getEmoji()));
    }

    private synchronized void react(MessageReactionAddEvent event){
        Answer answer = question.getAnswers().get(emotes.indexOf(MyEmote.getFromId(event.getReactionEmote().getEmoji())));
        if(printChoiceMsg) channel.sendMessage("Vous avez choisie : "+answer.getDescription()).queue();
        answer.toDoIfChose();
        msg.delete().queue();
        afterAnswerAction.accept(answer);
        done = true;
    }

    public void addPostEvent(Consumer<Answer> action) {
        this.afterAnswerAction = action;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
