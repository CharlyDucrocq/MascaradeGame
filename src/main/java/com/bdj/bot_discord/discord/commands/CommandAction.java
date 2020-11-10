package com.bdj.bot_discord.discord.commands;


import com.bdj.bot_discord.discord.ColorTheme;
import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.Lobby;
import com.bdj.bot_discord.mascarade_bot.errors.*;
import com.bdj.bot_discord.mascarade_bot.game.*;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.main.Application;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import com.bdj.bot_discord.mascarade_bot.utils.UserList;

import java.util.List;
import java.util.Random;

public class CommandAction {
    public static Application app;
    public static UserList userList;
    public static Random random = new Random();
    public static GameDistributor<MascaradeGame> lobbies = new GameDistributor<>();

    public static void create(MessageReceivedEvent event) {
        User user = getUser(event);
        DiscordLobby<MascaradeGame> lobby = lobbies.newLobby(user,event.getMessage().getChannel());
    }

    public static void join(MessageReceivedEvent event){
        User user = getUser(event);
        lobbies.joinLobby(user, event.getMessage().getChannel());
    }

    public static void start(MessageReceivedEvent event){
        User user = getUser(event);
        DiscordLobby<MascaradeGame> lobby = lobbies.getLobby(user);
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user)) throw new BadUser();

        lobby.createGame(new MascaradeFactory());
        lobby.getGame().start();
    }

    public static void switchAction(MessageReceivedEvent event){
        User userWhoAsk = getUser(event);
        MascaradeGame game = getGame(userWhoAsk);
        GameRound round = game.getRound();

        if(!userWhoAsk.equals(round.getUser())) throw new BadUser();

        List<net.dv8tion.jda.api.entities.User> list = event.getMessage().getMentionedUsers();
        if (list.size()>=2) throw new InvalidCommand();
        Player player = game.getTable().getPlayer(new User(list.get(0)));
        if (player==null) throw new InvalidCommand();

        round.switchCard(player, random.nextBoolean());
    }

    public static void peekAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        round.peekCharacter();
    }

    public static void characterChoice(MessageReceivedEvent event) {
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        String firstParam = extractFirstParameter(event.getMessage().getContentRaw());
        Character choice = Character.getFromText(firstParam);
        if (choice==null) throw new InvalidCommand();

        round.setCharacterToUse(choice);
    }

    public static void characterAction(MessageReceivedEvent event){
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        round.useCharacter();
    }

    public static void contestAction(MessageReceivedEvent event){
        User user = getUser(event);
        MascaradeGame game = getGame(user);
        GameRound round = game.getRound();

        if(user.equals(round.getUser())) throw new BadUser();

        Player player = game.getPlayer(user);
        if(player == null) throw new NotInTheGame();

        round.contest(player);
    }

    public static void recapPlayer(MessageReceivedEvent event) {
        User user = getUser(event);
        MascaradeGame game = getGame(user);
        game.getOut().printPlayerRecap(game.getTable().getPlayers());
    }

    public static void recapCharacter(MessageReceivedEvent event) {
        User user = getUser(event);
        MascaradeGame game = getGame(user);
        game.getOut().printCharactersRecap(game.getCharactersList());
    }

    public static void helpMsg(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Les Commandes", null);
        eb.setColor(ColorTheme.INFO.getColor());

        for (Command command : Command.values()) eb.addField(String.join("/",command.getEventCommands()), command.getDescription(), false);

        event.getChannel().sendMessage(eb.build()).queue();
    }

    private static MascaradeGame getGame(User user){
        return (MascaradeGame) lobbies.getGame(user);
    }

    private static User getUser(MessageReceivedEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    private static User getUserByCall(String call) {
        return userList.getUser(call);
    }

    private static String extractFirstParameter(String msg) {
        String[] tab1 = msg.split("[!][A-Za-z]+");
        if(tab1.length!=2) throw new InvalidCommand();
        String[] tab2 = tab1[1].split(" ");
        return tab2[1];
    }
}
