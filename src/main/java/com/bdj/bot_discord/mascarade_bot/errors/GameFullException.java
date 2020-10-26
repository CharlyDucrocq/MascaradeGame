package com.bdj.bot_discord.mascarade_bot.errors;

public class GameFullException extends GameException {
    @Override
    public String toString() {
        return "Le nombre maximum de joueur possible est déjà atteint.";
    }
}
