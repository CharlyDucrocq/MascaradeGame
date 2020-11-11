package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeOut;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.TableRound;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import com.bdj.bot_discord.mascarade_bot.utils.choice.YesOrNoQuestion;

public class Joker extends CardWithInteraction {
    private final Player[] players;
    private Player target1;
    private Player target2;
    MascaradeOut out ;

    public Joker(Player player, TableRound table, MascaradeOut out) {
        super(Character.SPY, player, out);
        players = table.getPlayers();
    }

    @Override
    protected QuestionAnswers actionBetweenLock() {
        return new ArraysChoice<Player>(
                "Quelle sera votre 1er victime ?",
                players,
                (choice1)->{
                    this.target1 = choice1;
                    player.ask(new ArraysChoice<Player>(
                            "Avec qui voulez vous échanger la carte de "+choice1.toString()+" ?",
                            players,
                            choice2 -> {
                                this.target2 = choice2;
                                player.ask(new YesOrNoQuestion(
                                        "Voulez vous échanger les cartes ?",
                                        () -> {
                                            Character c = target1.getCurrentCharacter();
                                            target1.setCurrentCharacter(target2.getCurrentCharacter());
                                            target2.setCurrentCharacter(c);
                                            player.getUser().sendMessage("Vous avez échanger les cartes de "+target1.toString()+" et "+target2.toString()+".");
                                            lock.unlock();
                                        },
                                        () -> {
                                            player.getUser().sendMessage("Vous n'avez pas échanger les cartes de "+target1.toString()+" et "+target2.toString()+".");
                                            lock.unlock();
                                        }
                                ));
                            }
                    ));
                });
    }

    public static Card create(Player player, MascaradeGame game){
        return new Joker(player, game.getTable(), game.getOut());
    }

    @Override
    public String getDescription() {
        return "Echange les cartes (ou pas) de "+ target1.toString()+" et "+target2.toString();
    }
}
