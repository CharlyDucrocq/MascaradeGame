package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.utils.GameDistributor;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getUser;

public class LobbyJoin<G extends Game> extends ErrorCatcherCommand {
    public final static String NAME = "join";
    private final GameDistributor<G> lobbies;

    public LobbyJoin(GameDistributor<G> lobbies){
        this.lobbies = lobbies;
        this.name = NAME;
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"joinGame"};
        this.help = "Rejoindre la partie courante.";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        lobbies.joinLobby(user, event.getMessage().getChannel());
    }
}
