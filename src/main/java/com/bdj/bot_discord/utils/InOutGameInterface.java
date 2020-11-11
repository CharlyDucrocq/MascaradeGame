package com.bdj.bot_discord.utils;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;

public interface InOutGameInterface {
    void printGlobalMsg(String message);
    void printPersonalMsg(User user, String message);
    void countDown(int from, String prefix, String suffix, String endMsg);
    int askChoiceTo(User user, QuestionAnswers describ);
}
