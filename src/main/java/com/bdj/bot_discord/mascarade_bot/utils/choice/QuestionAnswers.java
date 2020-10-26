package com.bdj.bot_discord.mascarade_bot.utils.choice;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuestionAnswers {
    String question = "?";
    List<Answer> list;

    public QuestionAnswers(Answer... tab){
        this.list = new LinkedList<>(Arrays.asList(tab));
    }

    public QuestionAnswers(List<Answer> list){
        this.list = new LinkedList<>(list);
    }

    public QuestionAnswers(String question, Answer... tab){
        this(tab);
        this.question = question;
    }

    public QuestionAnswers(String question, List<Answer> list){
        this(list);
        this.question = question;
    }
}
