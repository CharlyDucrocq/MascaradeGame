package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Queen extends Card {
    public final static int COIN_ADD = 2;

    public Queen(Player player) {
        super(Character.QUEEN, player);
    }

    @Override
    public void action() {
        player.getPurse().addCoin(COIN_ADD);
    }
}
