package com.bdj.bot_discord.mascarade_bot.main;

import com.bdj.bot_discord.mascarade_bot.discord.InOutDiscord;
import com.bdj.bot_discord.mascarade_bot.discord.commands.Command;
import com.bdj.bot_discord.mascarade_bot.discord.commands.CommandAction;
import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import com.bdj.bot_discord.mascarade_bot.utils.UserList;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Application extends ListenerAdapter {
    private UserList userList = new UserList();

    public Application() {
        super();
        CommandAction.app = this;
        CommandAction.inOut = new InOutDiscord();
        CommandAction.userList = this.userList;
    }

    @Override
    public synchronized void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        if(event.getAuthor().isBot()) return;
        System.out.println(event.getMessage().getContentRaw());
        for(Command command : Command.values()){
            if (command.isUsedIn(event.getMessage().getContentRaw().toLowerCase())){
                try {
                    command.doCommand(event);
                } catch (GameException e){
                    event.getChannel().sendMessage(e.getMessage()).queue();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] argv) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken("NzY3NzA0OTU0NDQ1MTAzMTI0.X41y9A.OdfngDcSDCWMbJPH3fr3nabUtNo");
        builder.addEventListeners(new Application());
        builder.build();
    }
}
