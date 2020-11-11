package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

public class QuestionToPlayer {
    QuestionAnswers question;
    Player player;

    public QuestionToPlayer set(QuestionAnswers question, Player player){
        clear();
        this.question = question;
        this.player = player;
        return this;
    }

    public QuestionToPlayer send(){
        return this;
    }

    public boolean noQuestionInProgress(){
        if (question == null) return true;
        return question.ended();
    }

    public void clear(){
        this.question = null;
        this.player = null;
    }
}
