package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public class Peasant extends Card {
    public final static int COIN_ADD_IF_ONE = 1;
    public final static int COIN_ADD_IF_TWO = 2;
    public final Verif getTwo;

    public Peasant(Player player, GameRound round) {
        super(Character.PEASANT, player);
        getTwo = ()-> !round.noContestPlayers() && round.howManyIn(Character.PEASANT) >= 2;
    }

    @Override
    public void action() {
        if(getTwo.verif())
            player.getPurse().addCoin(COIN_ADD_IF_TWO);
        else
            player.getPurse().addCoin(COIN_ADD_IF_ONE);
    }

    public static Card create(Player player, Game game){
        return new Peasant(player, game.getRound());
    }

    @Override
    public String getDescription() {
        if(getTwo.verif())
            return "Gagne "+COIN_ADD_IF_TWO+" pieces avec son compagnon";
        else
            return "Gagne "+COIN_ADD_IF_ONE+" piece car il est seul";
    }

    private static interface Verif{
        boolean verif();
    }
}
