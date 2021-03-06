package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.TableRound;

public class Thief extends Card {
    public final static int COIN_STOLE = 1;

    private final Player rightPlayer;
    private final Player leftPlayer;

    public Thief(Player player, TableRound table) {
        super(Character.THIEF, player);
        rightPlayer = table.getPrevOf(player);
        leftPlayer = table.getNextOf(player);
    }

    @Override
    public void action() {
        player.getPurse().addCoin(leftPlayer.getPurse().removeCoin(COIN_STOLE));
        player.getPurse().addCoin(rightPlayer.getPurse().removeCoin(COIN_STOLE));
    }

    public static Card create(Player player, MascaradeGame game){
        return new Thief(player, game.getTable());
    }

    @Override
    public String getDescription() {
        return "Vole "+COIN_STOLE+" a "+rightPlayer.toString()+" et a "+leftPlayer.toString();
    }
}
