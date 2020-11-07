package com.bdj.bot_discord.mascarade_bot.discord.commands;


import com.bdj.bot_discord.mascarade_bot.discord.InOutDiscord;
import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.*;
import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.Lobby;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.main.Application;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.bdj.bot_discord.mascarade_bot.utils.UserList;

import java.util.Random;

public class CommandAction {
    public static Application app;
    public static InOutDiscord inOut;
    private static Lobby lobby;
    private static Game game;
    public static UserList userList;
    public static Random random;

    public static void create(MessageReceivedEvent event) {
        inOut.setGlobalChannel(event.getMessage().getChannel());
        User user = getUser(event);
        lobby = new Lobby();
        lobby.setAdmin(user);
        inOut.printGlobalMsg("Partie créée. Admin : "+user.toString());
    }

    public static void join(MessageReceivedEvent event){
        if(lobby == null) throw new NoGameCreated();
        User user = getUser(event);
        if(!lobby.removePlayer(user)){
            lobby.addPlayer(user);
            inOut.printGlobalMsg(user.toString()+" a rejoin la partie");
        } else inOut.printGlobalMsg(user.toString()+" a quitté la partie");
    }

    public static void start(MessageReceivedEvent event){
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(getUser(event))) throw new BadUser();
        if(!lobby.haveEnoughPlayer()) throw new NotEnoughPlayers();
        game = lobby.createGame();
        game.setInOut(inOut);
        game.start();
    }

    public static void switchAction(MessageReceivedEvent event){
        User userWhoAsk = getUser(event);
        GameRound round = getActualRound();
        if(!userWhoAsk.equals(round.getUser())) throw new BadUser();
        String name = extractFirstParameter(event.getMessage().getContentRaw());
        Player player = getGame().getTable().getPlayerByName(name);
        if (player==null) throw new InvalidCommand();
        getActualRound().switchCard(player, random.nextBoolean());

//        UserQuestion userChoice = new UserQuestion(
//                "Avec qui voulez-vous échanger (ou pas) ?",
//                getGame().getUsers() ,
//                otherUser ->{
//                    Player otherPlayer = getGame().getPlayer(otherUser);
//                    YesOrNoQuestion yesOrNo = new YesOrNoQuestion(
//                            "Voulez-vous réellement échanger les cartes ?",
//                            () -> round.switchCard(otherPlayer, true),
//                            () -> round.switchCard(otherPlayer, false));
//                    inOut.askChoiceTo(userWhoAsk,yesOrNo);
//                });
//        inOut.askChoiceTo(userWhoAsk,userChoice);
    }

    public static void peekAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getActualRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        getActualRound().peekCharacter();
    }

    public static void characterChoice(MessageReceivedEvent event) {
        User user = getUser(event);
        GameRound round = getActualRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        String firstParam = extractFirstParameter(event.getMessage().getContentRaw());
        Character choice = Character.getFromText(firstParam);
        if (choice==null) throw new InvalidCommand();
        getActualRound().setCharacterToUse(choice);
    }

    private static String extractFirstParameter(String msg) {
        String[] tab1 = msg.split("[!][A-Za-z]+");
        if(tab1.length!=2) throw new InvalidCommand();
        String[] tab2 = tab1[1].split(" ");
        return tab2[1];
    }

    public static void characterAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getActualRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        getActualRound().useCharacter();
    }

    public static void contestAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getActualRound();
        if(user.equals(round.getUser())) throw new BadUser();
        Player player = getGame().getPlayer(user);
        if(player == null) throw new NotInTheGame();
        round.contest(player);
    }

    private static Game getGame(){
        if(game == null) throw new NoGameStarted();
        if(game.ended) throw new GameException("Game Ended ! Please restart the game.");
        return game;
    }

    private static GameRound getActualRound(){
        return getGame().getRound();
    }

    private static User getUser(MessageReceivedEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    public static void recapPlayer(MessageReceivedEvent messageReceivedEvent) {
        Game game = getGame();
        game.getOut().printPlayerRecap(game.getTable().getPlayers());
    }

    public static void recapCharacter(MessageReceivedEvent messageReceivedEvent) {
        Game game = getGame();
        game.getOut().printCharactersRecap(game.getCharactersList());
    }

    public static void helpMsg(MessageReceivedEvent event) {
        String toString = "";
        for (Command command : Command.values()) {
            toString+= " **- "+String.join("/",command.getEventCommands())+" :** "+command.getDescription()+"\n";
        }
        event.getChannel().sendMessage(toString).queue();//TODO
    }
}
