package com.bdj.bot_discord.mascarade_bot.discord;

import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;

public class InOutDiscord implements InOutGameInterface {
    private MessageChannel globalChannel;

    @Override
    public void printGlobalMsg(String message) {
        globalChannel.sendMessage(message).queue();
    }

    @Override
    public void printPersonalMsg(User user, String message) {
        user.sendMessage(message);
    }

    private CountDown countDown;

    @Override
    public void countDown(int from, String prefix, String suffix, String endMsg){
        if (countDown != null) countDown.kill();
        countDown = new CountDown(10,globalChannel,prefix,suffix,endMsg);
    }

    @Override
    public int askChoiceTo(User user, QuestionAnswers describ) {
        //tODO
        return 0;
    }

    public void printError(Exception e, MessageChannel channel) {
        if(channel == null) channel = this.globalChannel;

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Erreur", null);
        eb.setDescription(e.getMessage());
        eb.setColor(Color.red);

        channel.sendMessage(eb.build()).queue();
    }

    public void setGlobalChannel(MessageChannel channel) {
        this.globalChannel = channel;
    }

    public boolean noChannel() {
        return globalChannel == null;
    }

    public MessageChannel getGlobalChannel() {
        return globalChannel;
    }
}
