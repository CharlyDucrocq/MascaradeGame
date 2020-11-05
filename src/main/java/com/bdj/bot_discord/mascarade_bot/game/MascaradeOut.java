package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MascaradeOut {
    private InOutGameInterface inOut;

    public MascaradeOut(InOutGameInterface inOut){
        this.inOut = inOut;
    }

    public void printStart(Game game){
        inOut.printGlobalMsg(
                "############### *Début de la partie* ###############"
        );
        String partyDescription = "Liste des personnages :\n";
        for (Character character : game.getCharactersList()){
            partyDescription+= "\t" +character.toString() + " : "+character.getDescription()+"\n";
        }
        inOut.printGlobalMsg(partyDescription);
    }

    public void printPeek(Player player){
        inOut.printGlobalMsg(player.toString()+" regarde son role.");
        inOut.printPersonalMsg(player.getUser(),
                "Vous êtes " +player.getCurrentCharacter().toString()+"\n" +
                        "("+player.getCurrentCharacter().getDescription()+")"
        );
    }

    public void printStartTurn(GameRound gameRound) {
        inOut.printGlobalMsg("C'est au tour de "+gameRound.player.toString());
    }

    public void printPublicSwitch(Player player, Player other){
        inOut.printGlobalMsg(player.toString()+" échange (ou pas) ses cartes avec "+other.toString());
    }

    public void printContest(Player opponent) {
        inOut.printGlobalMsg(opponent.toString()+" s'oppose !");
    }

    public void printUncontest(Player opponent) {
        inOut.printGlobalMsg(opponent.toString()+" ne s'oppose plus !");
    }

    public void printSetCharacter(GameRound gameRound) {
        inOut.printGlobalMsg(gameRound.player.toString()+" affirme être "+gameRound.charaChose.toString());
    }


    public void printAction(Player player, Character charaChose) {
        inOut.printGlobalMsg(
                player.toString()+" utilise le pouvoir "+charaChose.toString()+"\n" +
                        "("+charaChose.getDescription()+")"
        );
    }

    public void printPenality(Player player) {
        inOut.printGlobalMsg(
                player.toString()+" : perdu, vous étiez "+player.getCurrentCharacter().toString()+"... \n" +
                        "Vous avez payé "+GlobalParameter.PENALTY+" piece(s) d'or à la banque."
        );
    }

    public void printEnd(Game game) {
        inOut.printGlobalMsg(
                "############### *Fin de la partie* ###############\n" +
                "##################################################"
        );
    }

    public void printPodium(TableRound tableRound) {
        String result = "#################### *Poduim* ####################\n*";
        int i=1;
        List<Player> players = Arrays.asList(tableRound.getPlayers());
        players.sort(Comparator.comparing(p->p.getPurse().getValue()));
        for (Player player : players) {
            result+=i+" - "+player.toString();
            if (i==1) result+="*";
            result+="\n";
            i++;
        }
        inOut.printGlobalMsg(result);
    }
}
