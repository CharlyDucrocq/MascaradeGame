package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getUser;
import static com.bdj.bot_discord.main.Application.mascaradeLobbies;

public class LobbyJoin<G extends Game> extends ErrorCatcherCommand {
    private final GameDistributor<G> lobbies;

    public LobbyJoin(GameDistributor<G> lobbies){
        this.lobbies = lobbies;
        this.name = "join";
        this.aliases = new String[]{"joinGame"};
        this.help = "Rejoindre la partie courante.";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        lobbies.joinLobby(user, event.getMessage().getChannel());
    }
}
