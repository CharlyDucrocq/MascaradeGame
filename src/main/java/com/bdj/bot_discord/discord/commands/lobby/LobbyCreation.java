package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.commands.MyRole;
import com.bdj.bot_discord.discord.lobby.PrivateChannels;
import com.bdj.bot_discord.discord.utils.*;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import static com.bdj.bot_discord.main.Application.*;

public class LobbyCreation<G extends Game> extends ErrorCatcherCommand {
    private final String prefix;
    private int channelId = 0;
    private final Class<G> gameClass;
    private GameDistributor<G> lobbies;

    public LobbyCreation(Class<G> gameClass, GameDistributor<G> lobbies, String prefix){
        this.lobbies = lobbies;
        this.gameClass = gameClass;
        this.prefix = prefix;

        this.name = "create";
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"createGame"};
        this.help = "Création d'une nouvelle partie.";
        this.requiredRole= MyRole.GAME_MASTER.toString();
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        PrivateChannels channels = new PrivateChannels(
                event.getGuild(),
                (channelId++)+"-"+gameClass.getSimpleName().replaceAll("(.)([A-Z])", "$1-$2"),
                event.getMessage().getCategory(),
                true
        );
        DiscordLobby<G> lobby = lobbies.newLobby(user, channels);
        activeEmoteListenerJoin(event.getMessage(), channels, lobby);
    }

    public void activeEmoteListenerJoin(Message msg, PrivateChannels channels, DiscordLobby<G> lobby){
        msg.addReaction(MyEmote.YES.getId()).queue();
        channels.setAnalyser(new ReactionAnalyser(msg)
                .addReaction(
                        MyEmote.YES,
                        (e)-> lobbies.joinLobby(getUser(e.getUser()),lobby),
                        (e)-> lobbies.joinLobby(getUser(e.getUser()),lobby)));
    }
}
