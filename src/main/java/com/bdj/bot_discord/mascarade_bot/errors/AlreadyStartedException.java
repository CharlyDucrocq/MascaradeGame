package com.bdj.bot_discord.mascarade_bot.errors;

public class AlreadyStartedException extends GameException {
    @Override
    public String toString() {
        return "La Partie a déjà commencé.";
    }
}
