package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.TableRound;

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

    public static Card create(Player player, MascaradeGame game) {
        return new Bishop(player, game.getTable());
    }

    @Override
    public String getDescription() {
        return "Prend "+COIN_TO_SWITCH+" pieces à "+richest.toString();
    }
}
