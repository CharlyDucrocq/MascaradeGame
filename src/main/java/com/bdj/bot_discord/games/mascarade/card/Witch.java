package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.*;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;

import java.util.List;

public class Witch extends Card {
    private final List<Player> players;
    private final MascaradeOut out;
    private Player target;

    public Witch(Player player, TableRound table, MascaradeOut out) {
        super(Character.WITCH, player);
        this.out =out;
        players = table.getPlayersWithout(player);
    }

    @Override
    public void action() {
        this.target = out.askForAPlayer(player, players, "Avec qui voulez vous échanger votre bourse ?", true);
        Purse richestPurse = target.getPurse();
        target.setPurse(player.getPurse());
        player.setPurse(richestPurse);
    }

    public static Card create(Player player, MascaradeGame game){
        return new Witch(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Echange sa bourse avec "+target;
    }
}
