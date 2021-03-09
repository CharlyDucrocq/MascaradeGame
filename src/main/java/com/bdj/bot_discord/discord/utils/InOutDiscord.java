package com.bdj.bot_discord.discord.utils;

import com.bdj.bot_discord.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.utils.InOutGameInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;

public class InOutDiscord implements InOutGameInterface {
    private TextChannel globalChannel;

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
        countDown = new CountDown(from,globalChannel,prefix,suffix,endMsg);
    }

    @Override
    public int askChoiceTo(User user, QuestionAnswers describ) {
        //tODO
        return 0;
    }

    public void printError(Exception e) {
        printError(e, globalChannel);
    }

    public static void printError(Exception e, MessageChannel channel){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Erreur", null);
        eb.setDescription(e.getMessage());
        eb.setColor(Color.red);

        channel.sendMessage(eb.build()).queue();
    }

    public void setGlobalChannel(TextChannel channel) {
        this.globalChannel = channel;
    }

    public boolean noChannel() {
        return globalChannel == null;
    }

    public TextChannel getGlobalChannel() {
        return globalChannel;
    }
}
