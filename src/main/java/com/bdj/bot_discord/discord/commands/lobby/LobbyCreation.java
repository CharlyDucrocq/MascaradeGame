package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.Lobby;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.mascaradeLobbies;
import static com.bdj.bot_discord.main.Application.*;

public class LobbyCreation<G extends Game> extends ErrorCatcherCommand {
    private GameDistributor<G> lobbies;

    public LobbyCreation(GameDistributor<G> lobbies){
        this.lobbies = lobbies;

        this.name = "create";
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"createGame"};
        this.help = "Création d'une nouvelle partie.";
        this.ownerCommand = true;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<G> lobby = lobbies.newLobby(user,event.getMessage().getChannel());
    }
}
