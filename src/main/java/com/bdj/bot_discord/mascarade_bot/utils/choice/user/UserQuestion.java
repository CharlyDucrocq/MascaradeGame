package com.bdj.bot_discord.mascarade_bot.utils.choice.user;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.choice.Answer;

public class UserQuestion extends QuestionAnswers {
    public UserQuestion(String question, User[] users, UserVoid whatToDo) {
        super(question,usersToChoices(users, whatToDo));
    }

    private static Answer[] usersToChoices(User[] users, UserVoid whatToDo){
        Answer[] answers = new Answer[users.length];
        int i=0;
        for(User user: users) answers[i++] = new Answer() {
            @Override
            public String getDescription() {
                return user.toString();
            }

            @Override
            public void toDoIfChose() {
                whatToDo.run(user);
            }
        };
        return answers;
    }
}

