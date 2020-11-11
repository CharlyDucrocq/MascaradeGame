package com.bdj.bot_discord.mascarade_bot.utils.choice;

import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.choice.Answer;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class ArraysChoice<T> extends QuestionAnswers {
    public ArraysChoice(String question, T[] tab, Consumer<T> whatToDo) {
        super(question,new Transformer<T>().toChoices(Arrays.asList(tab), whatToDo));
    }

    public ArraysChoice(String question, List<T> list, Consumer<T> whatToDo) {
        super(question,new Transformer<T>().toChoices(list, whatToDo));
    }

    private static class Transformer<T> {
        private Answer[] toChoices(Collection<T> entities, Consumer<T> whatToDo){
            Answer[] answers = new Answer[entities.size()];
            int i=0;
            for(T entity: entities) answers[i++] = new Answer() {
                @Override
                public String getDescription() {
                    return entity.toString();
                }

                @Override
                public void toDoIfChose() {
                    whatToDo.accept(entity);
                }
            };
            return answers;
        }
    }
}
