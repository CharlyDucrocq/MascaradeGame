package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.discord.ColorTheme;
import com.bdj.bot_discord.discord.CountDown;
import com.bdj.bot_discord.discord.commands.Command;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MascaradeOut {
    private MessageChannel channel;

    public MascaradeOut(MessageChannel channel){
        this.channel = channel;
    }

    public void printStart(MascaradeGame game){
        channel.sendMessage(
                "############### **Début de la partie** ###############\n"
        ).queue();
        printCharactersRecap(game.getCharactersList());
        String partyDescription = "Configuration de départ des Joueurs :\n";
        for (Player player : game.getTable().getPlayers()){
            partyDescription+= "\t**" +player.toString() + " :** "+player.getCurrentCharacter().toString()+"\n";
        }
        channel.sendMessage(partyDescription).queue();
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
        channel.sendMessage(playersRecap).queue();
    }

    public void printPeek(Player player){
        channel.sendMessage(player.toString()+" regarde son role.").queue();
        player.getUser().sendMessage(
                "Vous êtes " +player.getCurrentCharacter().toString()+"\n" +
                        "("+player.getCurrentCharacter().getDescription()+")"
        );
    }

    public void printStartTurn(GameRound gameRound) {
        channel.sendMessage("C'est au tour de "+gameRound.player.toString()).queue();
    }

    public void printSwitch(Player player, Player other, boolean trueOrNot){
        if(trueOrNot)
            player.getUser().sendMessage("Vous avez vraiment fait l'échange avec "+other.toString());
        else
            player.getUser().sendMessage("Vous n'avez pas fait l'échange avec "+other.toString());
        channel.sendMessage(player.toString()+" échange (ou pas) ses cartes avec "+other.toString()).queue();
    }

    public void printContest(Player opponent) {
        channel.sendMessage(opponent.toString()+" s'oppose !").queue();
    }

    public void printUncontest(Player opponent) {
        channel.sendMessage(opponent.toString()+" ne s'oppose plus !").queue();
    }

    private CountDown countDown;

    public void printSetCharacter(GameRound gameRound) {
        channel.sendMessage(gameRound.player.toString()+" affirme être "+gameRound.charaChose.toString()).queue();

        if (countDown != null) countDown.kill();
        countDown = new CountDown(GlobalParameter.CHOICE_USE_TIME_IN_SEC, channel,
                "Il reste ", "s avant de pouvoir utiliser le pouvoir",
                "Vous pouvez utiliser le pouvoir avec !"+ Command.USE.eventCommands[0]);
    }


    public void printAction(Player player, Card charaChose) {
        channel.sendMessage(
                player.toString()+" a utilisé le pouvoir "+charaChose.getType().toString()+"\n" +
                        "("+charaChose.getDescription()+")"
        ).queue();
    }

    public void printPenality(Player player) {
        channel.sendMessage(
                player.toString()+" : perdu, vous étiez "+player.getCurrentCharacter().toString()+"... \n" +
                        "Vous avez payé "+GlobalParameter.PENALTY+" piece(s) d'or à la banque."
        ).queue();
    }

    public void printEnd(MascaradeGame game) {
        channel.sendMessage(
                "############### **Fin de la partie** ###############\n" +
                "##################################################"
        ).queue();
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
        channel.sendMessage(result).queue();
    }
}
