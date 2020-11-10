package com.bdj.bot_discord.mascarade_bot.main;

import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.mascarade.MascaradeCommands;
import com.bdj.bot_discord.mascarade_bot.errors.InvalidCommand;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.bdj.bot_discord.mascarade_bot.utils.UserList;

//import com.bdj.bot_discord.discord.InOutDiscord;
//import com.bdj.bot_discord.discord.commands.Command;
//import com.bdj.bot_discord.mascarade_bot.errors.GameException;
//import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import java.util.Random;

import static com.bdj.bot_discord.mascarade_bot.main.TokenGhost.MY_ID;
import static com.bdj.bot_discord.mascarade_bot.main.TokenGhost.TOKEN;

public class Application extends ListenerAdapter {
    public static Random random = new Random();
    public static GameDistributor<MascaradeGame> mascaradeLobbies = new GameDistributor<>();
    public static UserList userList = new UserList();

    public static MascaradeGame getGame(User user){
        return (MascaradeGame) mascaradeLobbies.getGame(user);
    }

    public static User getUser(MessageReceivedEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    public static User getUser(CommandEvent event) {
        return userList.getUser(new User(event.getAuthor()));
    }

    public static User getUserByCall(String call) {
        return userList.getUser(call);
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
//        }
//    }

    public static void main(String[] argv) throws LoginException {
        JDABuilder builder = JDABuilder.createDefault(TOKEN);
        //builder.addEventListeners(new Application());

        builder.addEventListeners(new MascaradeCommands(MY_ID).build());
        builder.build();
    }
}
