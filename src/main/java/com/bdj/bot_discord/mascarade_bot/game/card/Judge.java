package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Bank;
import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Judge extends Card {
    Bank bank;

    public Judge(Player player, Bank bank) {
        super(Character.JUDGE,player);
        this.bank = bank;
    }

    @Override
    public void action() {
        bank.reverse(player);
    }

    public static Card create(Player player, Game game){
        return new Judge(player, game.getBank());
    }
}
