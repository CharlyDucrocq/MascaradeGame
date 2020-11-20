package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getUser;

public class RulesGetter extends ErrorCatcherCommand {
    private final String link;

    public RulesGetter(String link){
        this.link = link;

        this.name = "rules";
        this.category = MyCommandCategory.GAME_RULES;
        this.aliases = new String[]{"rulesInfo","gameInfo","game"};
        this.help = "Voir les règles du jeu.";
        this.guildOnly = false;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        event.getChannel().sendMessage(link).queue();
    }
}
