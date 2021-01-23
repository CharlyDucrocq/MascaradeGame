package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.MascaradeOut;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.TableRound;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.utils.choice.YesOrNoQuestion;

import java.util.List;

public class Spy extends Card {
    private final List<Player> players;
    private final MascaradeOut out;
    private Player target;

    public Spy(Player player, TableRound table, MascaradeOut out) {
        super(Character.SPY, player);
        this.out = out;
        players = table.getPlayersWithout(player);
    }

    @Override
    public void action() {
        this.target = out.askForAPlayer(player, players, "Quelle carte voulez vous regarder ?", true);
        out.printSpy(player, target);
        boolean really = out.askForABoolean(player, "Voulez vous échanger les cartes ?", true);
        if(really){
            Character c = player.getCurrentCharacter();
            player.setCurrentCharacter(target.getCurrentCharacter());
            target.setCurrentCharacter(c);
            player.getUser().sendMessage("Vous avez échanger les carte.");
        } else {
            player.getUser().sendMessage("Vous n'avez pas échanger les cartes");
        }
    }

    public static Card create(Player player, MascaradeGame game){
        return new Spy(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Regarde et echange sa carte (ou pas) avec "+target;
    }
}
