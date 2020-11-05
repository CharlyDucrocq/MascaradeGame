package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class King extends Card {
    public final static int COIN_ADD = 3;

    public King(Player player) {
        super(Character.KING, player);
    }

    @Override
    public void action() {
        player.getPurse().addCoin(COIN_ADD);
    }

    public static Card create(Player player, Game game){
        return new King(player);
    }
}
