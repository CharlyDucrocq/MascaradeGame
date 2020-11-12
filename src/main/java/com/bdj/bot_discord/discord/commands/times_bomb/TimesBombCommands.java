package com.bdj.bot_discord.discord.commands.times_bomb;

import com.bdj.bot_discord.discord.commands.lobby.LobbyCreation;
import com.bdj.bot_discord.discord.commands.lobby.LobbyJoin;
import com.bdj.bot_discord.discord.commands.lobby.StartGame;
import com.bdj.bot_discord.discord.commands.lobby.LobbyInfo;
import com.bdj.bot_discord.games.times_bomb.TimesBombFactory;
import com.bdj.bot_discord.main.Application;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import net.dv8tion.jda.api.entities.Activity;

public class TimesBombCommands extends CommandClientBuilder {
    public TimesBombCommands(String id){
        super();
        this.setOwnerId(id);
        String prefix = "tb!";
        this.setPrefix(prefix);
        this.setHelpWord("help");
        this.setActivity(Activity.playing("TimesBomb"));

        Command[] commands = new Command[]{
                new LobbyCreation<>(Application.timesBombLobbies),
                new LobbyJoin<>(Application.timesBombLobbies),
                new LobbyInfo(Application.timesBombLobbies, "Time's Bomb"),
                new StartGame<>(Application.timesBombLobbies, TimesBombFactory.class)
        };

        for (Command command : commands) this.addCommand(command);
    }
}

