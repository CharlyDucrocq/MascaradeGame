package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;

public class Queen extends Card {
    public final static int COIN_ADD = 2;

    public Queen(Player player) {
        super(Character.QUEEN, player);
    }

    @Override
    public void action() {
        player.getPurse().addCoin(COIN_ADD);
    }

    public static Card create(Player player, MascaradeGame game){
        return new Queen(player);
    }
}
