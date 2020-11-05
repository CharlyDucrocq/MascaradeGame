package com.bdj.bot_discord.mascarade_bot.discord;

import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import net.dv8tion.jda.api.entities.MessageChannel;

public class InOutDiscord implements InOutGameInterface {
    private MessageChannel globalChannel;

    @Override
    public void printGlobalMsg(String message) {
        globalChannel.sendMessage(message);
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

    @Override
    public void printError(Exception e) {
        globalChannel.sendMessage("*ERROR : "+e.getMessage()+"*");
    }

    public void setGlobalChannel(MessageChannel channel) {
        this.globalChannel = channel;
    }
}
