package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.MascaradeOut;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.TableRound;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.utils.choice.YesOrNoQuestion;

import java.util.List;

public class Joker extends Card {
    private final List<Player> players;
    private final MascaradeOut out;
    private Player target1;
    private Player target2;

    public Joker(Player player, TableRound table, MascaradeOut out) {
        super(Character.JOKER, player);
        this.out = out;
        players = table.getPlayersWithout(player);
    }

    @Override
    public void action() {
        this.target1 = out.askForAPlayer(player, players, "Quelle sera votre 1er victime ?", true);
        this.target2 = out.askForAPlayer(player, players, "Avec qui voulez vous échanger la carte de "+target1.toString()+" ?", true);
        boolean really = out.askForABoolean(player, "Voulez vous échanger les cartes ?", true);
        if(really){
            Character c = target1.getCurrentCharacter();
            target1.setCurrentCharacter(target2.getCurrentCharacter());
            target2.setCurrentCharacter(c);
            player.getUser().sendMessage("Vous avez échanger les cartes de "+target1.toString()+" et "+target2.toString()+".");
        }else {
            player.getUser().sendMessage("Vous n'avez pas échanger les cartes de "+target1.toString()+" et "+target2.toString()+".");
        }
    }

    public static Card create(Player player, MascaradeGame game){
        return new Joker(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Echange les cartes (ou pas) de "+ target1.toString()+" et "+target2.toString();
    }
}
