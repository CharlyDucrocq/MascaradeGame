package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Banque;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Judge extends Card {
    Banque banque;

    public Judge(Player player, Banque banque) {
        super(Character.JUDGE,player);
        this.banque = banque;
    }

    @Override
    public void action() {
        banque.reverse(player);
    }
}
