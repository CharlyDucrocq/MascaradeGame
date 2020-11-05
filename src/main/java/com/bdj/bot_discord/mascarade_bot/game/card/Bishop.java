package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.TableRound;

public class Bishop extends Card{
    public final static int COIN_TO_SWITCH = 2;

    private final Player richest;

    public Bishop(Player player, TableRound table) {
        super(Character.BISHOP, player);
        richest = table.getRichest(player);
    }

    @Override
    public void action() {
        player.getPurse().addCoin(richest.getPurse().removeCoin(COIN_TO_SWITCH));
    }

    public static Card create(Player player, Game game) {
        return new Bishop(player, game.getTable());
    }
}
