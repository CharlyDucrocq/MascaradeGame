package com.bdj.bot_discord.utils.choice;

import com.bdj.bot_discord.errors.InvalidCommand;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class QuestionAnswers {
    private String question = "?";
    private List<Answer> list;

    boolean answered = false;

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

    public void answer(String answer){
        try {
            int i = Integer.parseInt(answer);
            if(i<=0 || i>list.size()) throw new InvalidCommand();
            list.get(i).toDoIfChose();
        }
        catch (Exception e) {
            boolean fund = false;
            for (Answer a : list){
                if (a.getDescription().equals(answer)) {
                    fund = true;
                    a.toDoIfChose();
                }
            }
            if(!fund) throw new InvalidCommand();
        }
        answered = true;
    }

    public String getQuestion() {
        return question;
    }

    public List<Answer> getAnswers() {
        return list;
    }

    private boolean isInt(String s){
        try {
            Integer.parseInt(s);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean ended() {
        return answered;
    }
}
