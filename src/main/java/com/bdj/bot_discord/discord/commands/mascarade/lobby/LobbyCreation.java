package com.bdj.bot_discord.discord.commands.mascarade.lobby;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.mascaradeLobbies;
import static com.bdj.bot_discord.main.Application.*;

public class LobbyCreation extends ErrorCatcherCommand {

    public LobbyCreation(){
        this.name = "create";
        this.aliases = new String[]{"createGame"};
        this.help = "Création d'une nouvelle partie.";
        this.ownerCommand = true;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<MascaradeGame> lobby = mascaradeLobbies.newLobby(user,event.getMessage().getChannel());
    }
}
