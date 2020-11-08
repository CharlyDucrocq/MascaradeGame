package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

public class QuestionToPlayer {
    QuestionAnswers question;
    Player player;

    Type type;

    public enum Type{
        SWITCH_PLAYER,
        SWITCH_YES_NO,
        DEFAULT,
    }

    public Type getType() {
        return type;
    }

    public QuestionToPlayer set(QuestionAnswers question, Player player){
        return set(question, player, Type.DEFAULT);
    }

    public QuestionToPlayer set(QuestionAnswers question, Player player, Type type){
        clear();
        this.type = type;
        this.question = question;
        this.player = player;
        return this;
    }

    public QuestionToPlayer send(){
        player.ask(question);
        return this;
    }

    public boolean noQuestionInProgress(){
        if (question == null) return true;
        return question.ended();
    }

    public void clear(){
        if(this.player != null){
            player.clearQuestion();
        }
        this.question = null;
        this.player = null;
        this.type = null;
    }
}
