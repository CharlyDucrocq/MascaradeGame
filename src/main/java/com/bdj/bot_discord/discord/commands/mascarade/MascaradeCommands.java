package com.bdj.bot_discord.discord.commands.mascarade;

import com.bdj.bot_discord.discord.commands.TestCommand;
import com.bdj.bot_discord.discord.commands.mascarade.game.*;
import com.bdj.bot_discord.discord.commands.mascarade.info.CharacterRecap;
import com.bdj.bot_discord.discord.commands.mascarade.info.PlayerRecap;
import com.bdj.bot_discord.discord.commands.mascarade.lobby.LobbyCreation;
import com.bdj.bot_discord.discord.commands.mascarade.info.LobbyInfo;
import com.bdj.bot_discord.discord.commands.mascarade.lobby.LobbyJoin;
import com.bdj.bot_discord.discord.commands.mascarade.lobby.StartGame;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.User;

import java.util.Objects;

public class MascaradeCommands extends CommandClientBuilder {
    public MascaradeCommands(String id){
        super();
        this.setOwnerId(id);
        String prefix = "!";
        this.setPrefix(prefix);
        this.setHelpWord("help");
        this.setActivity(Activity.playing("Mascarade"));

        Command[] commands = new Command[]{
                new LobbyCreation(),
                new LobbyJoin(),
                new LobbyInfo(),
                new StartGame(),
//                new PeekAction(),
//                new SwitchAction(),
//                new ChoiceAction(),
//                new UseAction(),
//                new Contest(),
                new PlayerRecap(),
                new CharacterRecap(),
                new TestCommand()
        };

        for (Command command : commands) this.addCommand(command);

        this.setHelpConsumer((event) -> {
            StringBuilder builder = new StringBuilder("**"+event.getSelfUser().getName()+"** commands:\n");
            Command.Category category = null;
            for(Command command : commands)
            {
                if(!(command.isHidden() || (command.getCategory()!=null && !command.getCategory().test(event))) && (!command.isOwnerCommand() || event.isOwner()))
                {
                    if(!Objects.equals(category, command.getCategory()))
                    {
                        category = command.getCategory();
                        builder.append("\n\n  __").append(category==null ? "No Category" : category.getName()).append("__:\n");
                    }
                    builder.append("\n`").append(prefix).append(prefix==null?" ":"").append(command.getName())
                            .append(command.getArguments()==null ? "`" : " "+command.getArguments()+"`")
                            .append(" - ").append(command.getHelp());
                }
            }
            User owner = event.getJDA().getUserById(id);
            if(owner!=null)
            {
                builder.append("\n\nFor additional help, contact **").append(owner.getName()).append("**#").append(owner.getDiscriminator());
            }
            event.replyInDm(builder.toString(), unused ->
            {
                if(event.isFromType(ChannelType.TEXT))
                    event.reactSuccess();
            }, t -> event.replyWarning("Help cannot be sent because you are blocking Direct Messages."));
        });
    }
}
