package com.bdj.bot_discord.mascarade_bot.utils.choice.character;

import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.choice.Answer;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

public class CharacterChoice extends QuestionAnswers {
    public CharacterChoice(String question, Character[] chars, CharVoid whatToDo) {
        super(question,charsToChoices(chars, whatToDo));
    }

    private static Answer[] charsToChoices(Character[] characters, CharVoid whatToDo){
        Answer[] answers = new Answer[characters.length];
        int i=0;
        for(Character character: characters) answers[i++] = new Answer() {
            @Override
            public String getDescription() {
                return character.toString();
            }

            @Override
            public void toDoIfChose() {
                whatToDo.run(character);
            }
        };
        return answers;
    }
}
