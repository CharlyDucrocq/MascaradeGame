package com.bdj.bot_discord.discord.commands.mascarade;

import com.bdj.bot_discord.discord.commands.mascarade.game.QuestionAgain;
import com.bdj.bot_discord.discord.utils.MyEmote;
import com.bdj.bot_discord.discord.commands.TestCommand;
import com.bdj.bot_discord.discord.commands.lobby.*;
import com.bdj.bot_discord.discord.commands.mascarade.info.CharacterRecap;
import com.bdj.bot_discord.discord.commands.mascarade.info.PlayerRecap;
import com.bdj.bot_discord.games.mascarade.MascaradeFactory;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.main.Application;
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
        String prefix = "m!";
        this.setPrefix(prefix);
        this.setHelpWord("help");
        this.setActivity(Activity.playing(MyEmote.DICE.getId()));

        Command[] commands = new Command[]{
                new LobbyCreation<>(MascaradeGame.class, Application.mascaradeLobbies, prefix),
                new LobbyJoin<>(Application.mascaradeLobbies),
                new LobbyInfo<>(Application.mascaradeLobbies, "Mascarade"),
                new StartGame<>(Application.mascaradeLobbies, MascaradeFactory.class),
                new KillGame<>(Application.timesBombLobbies),
                new MasterClean<>(Application.timesBombLobbies),
                new GiveAdminAccess<>(Application.timesBombLobbies),
                new QuestionAgain(),
                new RulesGetter("http://jeuxstrategie1.free.fr/jeu_mascarade/regle.pdf"),
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
