package utils.choice.user;

import discord.User;
import utils.choice.QuestionAnswers;
import utils.choice.Answer;

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

