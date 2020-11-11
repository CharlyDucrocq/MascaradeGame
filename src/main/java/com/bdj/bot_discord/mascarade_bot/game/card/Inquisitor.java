package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.*;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;

import java.util.List;

public class Inquisitor  extends CardWithInteraction {
    public final static int TAX_COIN = 4;

    public final Player[] players;
    public final List<Character> characters;

    public Player target;
    private Character charAnnounced;
    int gift = 0;

    public Inquisitor(Player player, MascaradeGame game) {
        super(Character.INQUISITOR, player, game.getOut());
        players = game.getTable().getPlayers();
        characters = game.getCharactersList();
    }

    @Override
    protected QuestionAnswers actionBetweenLock() {
        out.inquisitorProceed(player, this);
        return null;
    }

    public void processInfo(Player target, Character character) {
        this.target = target;
        this.charAnnounced = character;
        if (charAnnounced != target.getCurrentCharacter()){
            gift = target.getPurse().removeCoin(TAX_COIN);
            player.getPurse().addCoin(gift);
        }
        lock.unlock();
    }

    @Override
    public String getDescription() {
        if (gift>0)
            return "Se voit offert "+gift+" piece par "+target.toString()+". " +
                    "Celui ci à affirmer être "+ charAnnounced.toString()+" alors qu'il était "+target.getCurrentCharacter().toString()+".";
        else
            return "Ne gagne rien de la part de "+target.toString()+". " +
                    "Celui-ci à correctement deviné sa carte "+ charAnnounced.toString()+".";
    }
}
