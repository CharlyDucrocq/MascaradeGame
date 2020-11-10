package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Beggar extends Card {


    public Beggar(Player player) {
        super(Character.BEGGAR, player);
    }

    @Override
    public void action() {
        //nothing
    }


    public static Card create(Player player, MascaradeGame game){
        return new Beggar(player);
    }
}
