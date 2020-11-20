package com.bdj.bot_discord.discord.commands.times_bomb;

import com.bdj.bot_discord.discord.MyEmote;
import com.bdj.bot_discord.discord.commands.lobby.*;
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
        this.setActivity(Activity.playing(MyEmote.DICE.getId()));

        Command[] commands = new Command[]{
                new LobbyCreation<>(Application.timesBombLobbies),
                new LobbyJoin<>(Application.timesBombLobbies),
                new LobbyInfo<>(Application.timesBombLobbies, "Time's Bomb"),
                new StartGame<>(Application.timesBombLobbies, TimesBombFactory.class),
                new RulesGetter("https://www.magicbazar.fr/pdf/rules_games/time_bomb_regles_fr.pdf"),
        };

        for (Command command : commands) this.addCommand(command);
    }
}

