package com.bdj.bot_discord.mascarade_bot.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;

public class CountDown implements Runnable {
    private int current = 0;
    private MessageChannel channel;
    private Message message;
    private boolean on = true;

    String prefix = "Temps restant : ";
    String suffix = "";
    String endMsg = null;

    public CountDown(int from, MessageChannel on, String prefix,String suffix){
        this.prefix = prefix;
        this.suffix = suffix;
        current = from;
        channel = on;
        new Thread(this).start();
    }

    public CountDown(int from, MessageChannel on, String endMsg){
        this.endMsg = endMsg;
        this.current = from;
        this.channel = on;
        new Thread(this).start();
    }

    public CountDown(int from, MessageChannel on){
        current = from;
        channel = on;
        new Thread(this).start();
    }

    public CountDown(int from, MessageChannel on, String prefix, String suffix, String endMsg) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.endMsg = endMsg;
        this.current = from;
        this.channel = on;
        new Thread(this).start();
    }

    @Override
    public void run() {
        createMsg();
        while (current>0 && on){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(on) message.editMessage(prefix+--current+suffix).queue();
        }
        if (on) deleteMessage();
    }

    private synchronized void deleteMessage() {
        if(message != null){
            if (endMsg == null || !on) {
                message.delete().queue();
            } else {
                message.editMessage(endMsg).queue();
            }
        }
    }

    private synchronized void createMsg() {
        if(on) message = channel.sendMessage(prefix+current+suffix).complete();
    }

    public synchronized void kill(){
        on = false;
        deleteMessage();
    }
}
