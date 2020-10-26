package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.utils.ResourcesAccess;

public enum Character {
    SPY,
    BISHOP,
    JUDGE,
    THIEF,
    QUEEN,
    KING,
    WITCH,
    PEASANT,
    JOKER,
    CHEATER,
    WIDOW,
    INQUISITOR,
    BEGGAR;

    private static ResourcesAccess RESOURCES_ACCESS = new ResourcesAccess("text/card/describ");

    public String getDescription() {
        return RESOURCES_ACCESS.getFileInString(this.toString()+".txt");
    }

    @Override
    public String toString() {
        // TODO : francais
        return this.name().toLowerCase();
    }
}
