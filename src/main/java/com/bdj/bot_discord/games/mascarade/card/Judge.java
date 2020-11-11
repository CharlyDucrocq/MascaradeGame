package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.Bank;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;

public class Judge extends Card {
    Bank bank;
    int value;

    public Judge(Player player, Bank bank) {
        super(Character.JUDGE,player);
        this.bank = bank;
        value = bank.getStock();
    }

    @Override
    public void action() {
        bank.reverse(player);
    }

    public static Card create(Player player, MascaradeGame game){
        return new Judge(player, game.getBank());
    }

    @Override
    public String getDescription() {
        return "Récupère les "+value+" pièces payés en amende à la banque";
    }
}
