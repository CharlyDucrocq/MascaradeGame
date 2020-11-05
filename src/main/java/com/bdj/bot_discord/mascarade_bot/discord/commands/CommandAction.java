package com.bdj.bot_discord.mascarade_bot.discord.commands;


import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.errors.*;
import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.Lobby;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.main.Application;
import com.bdj.bot_discord.mascarade_bot.utils.choice.character.CharacterChoice;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import com.bdj.bot_discord.mascarade_bot.utils.choice.user.UserQuestion;
import com.bdj.bot_discord.mascarade_bot.utils.UserList;
import com.bdj.bot_discord.mascarade_bot.utils.choice.YesOrNoQuestion;

public class CommandAction {
    public static Application app;
    public static InOutGameInterface inOut;
    private static Lobby lobby;
    private static Game game;
    public static UserList userList;

    public static void create(MessageReceivedEvent event) {
        User user = getUser(event);
        lobby = new Lobby();
        lobby.addAdmin(user);
    }

    public static void join(MessageReceivedEvent event){
        if(lobby == null) throw new NoGameCreated();
        User user = getUser(event);
        lobby.addPlayer(user);
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
        UserQuestion userChoice = new UserQuestion(
                "Avec qui voulez-vous échanger (ou pas) ?",
                getGame().getUsers() ,
                otherUser ->{
                    Player otherPlayer = getGame().getPlayer(otherUser);
                    YesOrNoQuestion yesOrNo = new YesOrNoQuestion(
                            "Voulez-vous réellement échanger les cartes ?",
                            () -> round.switchCard(otherPlayer, true),
                            () -> round.switchCard(otherPlayer, false));
                    inOut.askChoiceTo(userWhoAsk,yesOrNo);
                });
        inOut.askChoiceTo(userWhoAsk,userChoice);
    }

    public static void peekAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getActualRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        getActualRound().peekCharacter();
    }

    public static void characterAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getActualRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        // TODO
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
        return game;
    }

    private static GameRound getActualRound(){
        return getGame().getRound();
    }

    private static User getUser(MessageReceivedEvent event) {
        return userList.getUser(new User(event.getMember()));
    }
}
