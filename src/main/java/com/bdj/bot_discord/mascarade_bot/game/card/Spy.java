package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.*;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.choice.YesOrNoQuestion;

public class Spy extends CardWithInteraction {
    private final Player[] players;
    private Player target;
    MascaradeOut out ;

    public Spy(Player player, TableRound table, MascaradeOut out) {
        super(Character.SPY, player, out);
        players = table.getPlayers();
    }

    @Override
    protected QuestionAnswers actionBetweenLock() {
        return new ArraysChoice<Player>(
                "Quelle carte voulez vous regarder ?",
                players,
                (target)->{
                    this.target = target;
                    out.printSpy(player, target);
                    player.ask(new YesOrNoQuestion(
                            "Voulez vous échanger les cartes ?",
                            () -> {
                                Character c = player.getCurrentCharacter();
                                player.setCurrentCharacter(target.getCurrentCharacter());
                                target.setCurrentCharacter(c);
                                player.getUser().sendMessage("Vous avez échanger les carte.");
                                lock.unlock();
                            },
                            () -> {
                                player.getUser().sendMessage("Vous n'avez pas échanger les cartes");
                                lock.unlock();
                            }
                    ));
                });
    }

    public static Card create(Player player, MascaradeGame game){
        return new Spy(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Regarde et echange sa carte (ou pas) avec "+target;
    }
}
