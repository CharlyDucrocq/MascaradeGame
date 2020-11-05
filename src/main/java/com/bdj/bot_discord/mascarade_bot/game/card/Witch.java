package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.Purse;
import com.bdj.bot_discord.mascarade_bot.game.TableRound;

public class Witch extends Card {
    private final Player richest;

    public Witch(Player player, TableRound table) {
        super(Character.WITCH, player);
        richest = table.getRichest(player);
    }

    @Override
    public void action() {
        Purse richestPurse = richest.getPurse();
        richest.setPurse(player.getPurse());
        player.setPurse(richestPurse);
    }
}
