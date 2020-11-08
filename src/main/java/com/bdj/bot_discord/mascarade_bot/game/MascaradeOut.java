package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.ColorTheme;
import com.bdj.bot_discord.mascarade_bot.discord.CountDown;
import com.bdj.bot_discord.mascarade_bot.discord.InOutDiscord;
import com.bdj.bot_discord.mascarade_bot.discord.commands.Command;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MascaradeOut {
    private InOutGameInterface inOut;
    private MessageChannel channel;

    public MascaradeOut(InOutDiscord inOut){
        this.inOut = inOut;
        this.channel = inOut.getGlobalChannel();
    }

    public void printStart(Game game){
        inOut.printGlobalMsg(
                "############### **Début de la partie** ###############\n"
        );
        printCharactersRecap(game.getCharactersList());
        String partyDescription = "Configuration de départ des Joueurs :\n";
        for (Player player : game.getTable().getPlayers()){
            partyDescription+= "\t**" +player.toString() + " :** "+player.getCurrentCharacter().toString()+"\n";
        }
        inOut.printGlobalMsg(partyDescription);
    }

    public void printCharactersRecap(List<Character> charactersList) {

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Les Personnages", null);

        eb.setColor(ColorTheme.INFO.getColor());

        for (Character character : charactersList) eb.addField(character.toString(), character.getDescription(), false);

        channel.sendMessage(eb.build()).queue();
    }

    public void printPlayerRecap(Player[] players){
        String playersRecap = "**Situation actuel :**\n";
        for(Player player:players) playersRecap+= " - "+player.toString()+"\n";
        inOut.printGlobalMsg(playersRecap);
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

    public void printSwitch(Player player, Player other, boolean trueOrNot){
        if(trueOrNot)
            inOut.printPersonalMsg(player.getUser(),"Vous avez vraiment fait l'échange avec "+other.toString());
        else
            inOut.printPersonalMsg(player.getUser(),"Vous n'avez pas fait l'échange avec "+other.toString());
        inOut.printGlobalMsg(player.toString()+" échange (ou pas) ses cartes avec "+other.toString());
    }

    public void printContest(Player opponent) {
        inOut.printGlobalMsg(opponent.toString()+" s'oppose !");
    }

    public void printUncontest(Player opponent) {
        inOut.printGlobalMsg(opponent.toString()+" ne s'oppose plus !");
    }

    private CountDown countDown;

    public void printSetCharacter(GameRound gameRound) {
        inOut.printGlobalMsg(gameRound.player.toString()+" affirme être "+gameRound.charaChose.toString());
        inOut.countDown(GlobalParameter.CHOICE_USE_TIME_IN_SEC,
                "Il reste ", "s avant de pouvoir utiliser le pouvoir",
                "Vous pouvez utiliser le pouvoir avec !"+ Command.USE.eventCommands[0]);
    }


    public void printAction(Player player, Card charaChose) {
        inOut.printGlobalMsg(
                player.toString()+" a utilisé le pouvoir "+charaChose.getType().toString()+"\n" +
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
                "############### **Fin de la partie** ###############\n" +
                "##################################################"
        );
    }

    public void printPodium(TableRound tableRound) {
        String result = "#################### **Poduim** ####################\n**";
        int i=1;
        List<Player> players = Arrays.asList(tableRound.getPlayers());
        players.sort(Comparator.comparing(p->(-p.getPurse().getValue())));
        for (Player player : players) {
            result+=i+" - "+player.toString();
            if (i==1) result+="**";
            result+="\n";
            i++;
        }
        inOut.printGlobalMsg(result);
    }
}
