package com.bdj.bot_discord.mascarade_bot.errors;

public class QuestionInProgress extends GameException {
    public QuestionInProgress(){
        super("Une question est en cours. Vous n'êtes pas autorisé à faire ça tant que la réponse n'a pas été envoyé.");
    }
}
