package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.*;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;

import java.util.List;

public class Witch extends CardWithInteraction {
    private final List<Player> players;
    private Player target;

    public Witch(Player player, TableRound table, MascaradeOut out) {
        super(Character.WITCH, player, out);
        players = table.getPlayersWithout(player);
    }

    @Override
    protected QuestionAnswers actionBetweenLock() {
        return new ArraysChoice<>(
                "Avec qui voulez vous échanger votre bourse ?",
                players,
                (target)->{
                    Purse richestPurse = target.getPurse();
                    target.setPurse(player.getPurse());
                    player.setPurse(richestPurse);
                    this.target = target;
                    lock.unlock();
                });
    }

    public static Card create(Player player, MascaradeGame game){
        return new Witch(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Echange sa bourse avec "+target;
    }
}
