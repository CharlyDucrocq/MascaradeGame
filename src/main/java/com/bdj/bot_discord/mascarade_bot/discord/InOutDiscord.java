package com.bdj.bot_discord.mascarade_bot.discord;

import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import net.dv8tion.jda.api.entities.MessageChannel;

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

    @Override
    public void countDown(int from) {
        //tODO
    }

    @Override
    public int askChoiceTo(User user, QuestionAnswers describ) {
        //tODO
        return 0;
    }

    public void printError(Exception e, MessageChannel channel) {
        if(channel == null) channel = this.globalChannel;
        channel.sendMessage("```diff\n- "+"ERROR : "+e.getMessage()+"```").queue();
    }

    public void setGlobalChannel(MessageChannel channel) {
        this.globalChannel = channel;
    }

    public boolean noChannel() {
        return globalChannel == null;
    }
}
