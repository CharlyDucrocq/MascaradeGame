package com.bdj.bot_discord.discord;

import com.bdj.bot_discord.main.Application;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ReactionAnalyser {
    private Map<MyEmote, Boolean> killed = new HashMap<>();
    private User target = null;
    private Message msg;

    public ReactionAnalyser(Message msg){
        this.msg = msg;
    }

    public void addReaction(MyEmote emote, Consumer<MessageReactionAddEvent> ifAdd, Consumer<MessageReactionRemoveEvent> ifRemove){
        killed.put(emote, Boolean.FALSE);
        msg.addReaction(emote.getId()).queue();
        addReaction(emote, ifAdd);
        removeReaction(emote, ifRemove);
    }

    private void addReaction(MyEmote emote, Consumer<MessageReactionAddEvent> ifAdd){
        if (killed.get(emote)) return;
        Application.waiter.waitForEvent(MessageReactionAddEvent.class,e -> check(e, emote), e->{
            synchronized (this) {
                ifAdd.accept(e);
                addReaction(emote, ifAdd);
            }
        });
    }

    private void removeReaction(MyEmote emote, Consumer<MessageReactionRemoveEvent> ifRemove){
        if (killed.get(emote)) return;
        Application.waiter.waitForEvent(MessageReactionRemoveEvent.class,e -> check(e, emote), e->{
            synchronized (this) {
                ifRemove.accept(e);
                removeReaction(emote, ifRemove);
            }
        });
    }

    private synchronized boolean check(MessageReactionAddEvent e, MyEmote emote){
        User user = e.getUser();
        if(user == null) return false;
        return !e.getUser().isBot() &&
                e.getMessageId().equals(msg.getId()) &&
                e.getReactionEmote().getEmoji().equals(emote.getId()) &&
                (target == null || target.equals(e.getUser()));
    }

    private synchronized boolean check(MessageReactionRemoveEvent e, MyEmote emote){
        return e.getMessageId().equals(msg.getId()) &&
                e.getReactionEmote().getEmoji().equals(emote.getId()) &&
                (target == null || target.equals(e.getUser()));
    }

    public synchronized void kill(MyEmote emote){
        killed.put(emote, Boolean.TRUE);
    }

    public synchronized void killAll(){
        for (MyEmote emote : killed.keySet()){
            kill(emote);
        }
    }
}
