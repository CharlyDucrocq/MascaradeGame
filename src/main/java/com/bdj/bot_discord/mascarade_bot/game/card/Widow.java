package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Widow extends Card {
    public Widow(Player player) {
        super(Character.WIDOW, player);
    }

    @Override
    public void action() {
        int diff = 10-player.getPurse().getValue();
        if (diff>0)
            player.getPurse().addCoin(diff);
    }

    public static Card create(Player player, MascaradeGame game){
        return new Widow(player);
    }
}
