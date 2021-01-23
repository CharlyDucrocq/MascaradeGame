package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.MascaradeOut;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;

import java.util.List;

public class Inquisitor  extends Card {
    public final static int TAX_COIN = 4;

    public final List<Player> players;
    public final List<Character> characters;
    private final MascaradeOut out;

    public Player target;
    private Character charAnnounced;
    int gift = 0;

    public Inquisitor(Player player, MascaradeGame game) {
        super(Character.INQUISITOR, player);
        this.out =game.getOut();
        players = game.getTable().getPlayersWithout(player);
        characters = game.getCharactersList();
    }

    @Override
    public void action() {
        this.target = out.askForAPlayer(player, players, "En tant qu'inquisiteur, à qui demandez-vous de deviner sa carte ?", false);
        this.charAnnounced = out.askForAChar(target, characters, "Quelle est d'après vous votre carte ?", false);
        if (charAnnounced != target.getCurrentCharacter()){
            gift = target.getPurse().removeCoin(TAX_COIN);
            player.getPurse().addCoin(gift);
        }
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
