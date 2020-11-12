package com.bdj.bot_discord.main;

import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.mascarade.MascaradeCommands;
import com.bdj.bot_discord.discord.commands.times_bomb.TimesBombCommands;
import com.bdj.bot_discord.errors.InvalidCommand;
import com.bdj.bot_discord.games.mascarade.GlobalParameter;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.times_bomb.TimesBombFactory;
import com.bdj.bot_discord.games.times_bomb.TimesBombGame;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.bdj.bot_discord.utils.UserList;

//import com.bdj.bot_discord.discord.InOutDiscord;
//import com.bdj.bot_discord.discord.commands.Command;
//import com.bdj.bot_discord.errors.GameException;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import java.util.Random;

import static com.bdj.bot_discord.main.TokenGhost.MY_ID;
import static com.bdj.bot_discord.main.TokenGhost.TOKEN;

public class Application extends ListenerAdapter {
    public static Random random = new Random();
    public static GameDistributor<MascaradeGame> mascaradeLobbies = new GameDistributor<>(GlobalParameter.MAX_PLAYERS);
    public static GameDistributor<TimesBombGame> timesBombLobbies = new GameDistributor<>(TimesBombFactory.MAX_PLAYER);
    public static UserList userList = new UserList();

    public static MascaradeGame getMascaradeGame(User user){
        return (MascaradeGame) mascaradeLobbies.getGame(user);
    }

    public static TimesBombGame getTBGame(User user){
        return (TimesBombGame) timesBombLobbies.getGame(user);
    }

    public static User getUser(MessageReceivedEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    public static User getUser(CommandEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    public static String extractFirstParameter(String msg) {
        String[] tab1 = msg.split("[!][A-Za-z]+");
        if(tab1.length!=2) throw new InvalidCommand();
        String[] tab2 = tab1[1].split(" ");
        return tab2[1];
    }
//
//    private InOutDiscord inOut=new InOutDiscord();
//
//    public Application() {
//        super();
//    }
//
//    @Override
//    public synchronized void onMessageReceived(@Nonnull MessageReceivedEvent event) {
//        if(event.getAuthor().isBot()) return;
//        System.out.println(event.getMessage().getContentRaw());
//        for(Command command : Command.values()){
//            if (command.isUsedIn(event.getMessage().getContentRaw().toLowerCase())){
//                try {
//                    command.doCommand(event);
//                } catch (GameException e){
//                    InOutDiscord.printError(e,event.getChannel());
//                } catch (Exception e){
//                    e.printStackTrace(System.out);
//                }
//            }
////        }
//    @Override
//    public synchronized void onMessageReceived(@Nonnull MessageReceivedEvent event) {
//        if(event.getAuthor().isBot()) return;
//        event.getMessage().
//    }

    public static EventWaiter waiter = new EventWaiter();

    public static void main(String[] argv) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        //builder.addEventListeners(new Application());

        builder.addEventListeners(new MascaradeCommands(MY_ID).build());
        builder.addEventListeners(new TimesBombCommands(MY_ID).build());
        builder.addEventListeners(waiter);
        builder.build();
    }
}
