package main;

import discord.InOutDiscord;
import discord.commands.Command;
import discord.commands.CommandAction;
import errors.GameException;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import utils.UserList;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

public class Application extends ListenerAdapter {
    private UserList userList = new UserList();

    public

    Application() {
        super();
        CommandAction.app = this;
        CommandAction.inOut = new InOutDiscord();
        CommandAction.userList = this.userList;
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {
        for(Command command : Command.values()){
            if (command.isUsedIn(event.getMessage().getContentRaw().toLowerCase()))
                try {
                    command.doCommand(event);
                } catch (GameException e){
                    //TODO
                }
        }
    }

    public static void main(String[] argv) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        builder.setToken(argv[1]);
        builder.addEventListeners(new Application());
        builder.build();
    }
}
