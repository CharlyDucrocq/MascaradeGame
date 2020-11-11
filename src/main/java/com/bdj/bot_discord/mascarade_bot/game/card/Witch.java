package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.Purse;
import com.bdj.bot_discord.mascarade_bot.game.TableRound;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Witch extends CardWithInteraction {
    private final Lock lock = new ReentrantLock();
    private final Player[] players;
    private Player target;

    public Witch(Player player, TableRound table) {
        super(Character.WITCH, player);
        players = table.getPlayers();
    }

    @Override
    protected QuestionAnswers actionBetweenLock() {
        return new ArraysChoice<Player>(
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
        return new Witch(player, game.getTable());
    }

    @Override
    public String getDescription() {
        return "Echange sa bourse avec "+target;
    }
}
