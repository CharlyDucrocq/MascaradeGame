package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;

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
