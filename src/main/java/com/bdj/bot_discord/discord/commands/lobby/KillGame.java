package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.commands.MyRole;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.utils.GameDistributor;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.NoGameCreated;
import com.bdj.bot_discord.errors.NoGameStarted;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.Objects;

import static com.bdj.bot_discord.main.Application.getUser;

public class KillGame<G extends Game> extends ErrorCatcherCommand {
    private final GameDistributor<G> lobbies;

    public KillGame(GameDistributor<G> lobbies){
        this.lobbies = lobbies;

        this.name = "kill";
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"gameKill","killGame"};
        this.help = "Termine de force une partie.";
        this.requiredRole= MyRole.GAME_MASTER.toString();

        this.ownerCommand=true;
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<G> lobby = lobbies.getLobby(event.getChannel());

        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user) &&
                event.getMember().getRoles().stream().map(Objects::toString).noneMatch(MyRole.GAME_MASTER.toString()::equals))
            throw new BadUser();
        if (lobby.gameOver())
            throw new NoGameStarted();

        lobby.killGame();
    }
}
