package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Cheater extends Card {
    public final static int GOAL_FOR_WIN = 10;
    public Cheater( Player player) {
        super(Character.CHEATER, player);
    }

    @Override
    public void action() {
        player.cheat();
    }

    public static Card create(Player player, MascaradeGame game){
        return new Cheater(player);
    }
}
