package utils;

import discord.User;
import utils.choice.QuestionAnswers;

public interface InOutGameInterface {
    void printGlobalMsg(String message);
    void printPersonalMsg(User user, String message);
    void countDown(int from);
    int askChoiceTo(User user, QuestionAnswers describ);
}
