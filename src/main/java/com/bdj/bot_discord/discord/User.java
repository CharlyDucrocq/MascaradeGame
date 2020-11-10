package com.bdj.bot_discord.discord;

import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

import java.util.Objects;

public class User {
    private net.dv8tion.jda.api.entities.User user;
    private QuestionAnswers questionInProgress;

    public User(net.dv8tion.jda.api.entities.User user){
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public void sendMessage(String message) {
        user.openPrivateChannel().complete().sendMessage(message).queue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof String) {
            String name = ((String) o).toLowerCase();
            return name.equals(toString())
                    || name.equals(getName());
        }
        if (getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.user, user.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return user.getAsMention();
    }

    public void ask(QuestionAnswers question) {
        questionInProgress = question;
        new InOutDiscord().askChoiceTo(this,question);
    }

    public void answer(String answer){
        if(questionInProgress==null) throw new GameException("Aucune question en cours");
        questionInProgress.answer(answer);
        questionInProgress = null;
    }

    public void clearQuestion() {
        questionInProgress = null;
    }
}
