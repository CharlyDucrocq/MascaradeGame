package com.bdj.bot_discord.discord.commands.mascarade.lobby;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getUser;
import static com.bdj.bot_discord.main.Application.mascaradeLobbies;

public class LobbyJoin extends ErrorCatcherCommand {
    public LobbyJoin(){
        this.name = "join";
        this.aliases = new String[]{"joinGame"};
        this.help = "Rejoindre la partie courante.";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        mascaradeLobbies.joinLobby(user, event.getMessage().getChannel());
    }
}
