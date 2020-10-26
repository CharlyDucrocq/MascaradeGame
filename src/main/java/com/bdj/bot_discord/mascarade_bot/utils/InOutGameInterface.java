package com.bdj.bot_discord.mascarade_bot.utils;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

public interface InOutGameInterface {
    void printGlobalMsg(String message);
    void printPersonalMsg(User user, String message);
    void countDown(int from);
    int askChoiceTo(User user, QuestionAnswers describ);
}
