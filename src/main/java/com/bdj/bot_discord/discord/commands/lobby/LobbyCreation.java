package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.commands.MyRole;
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
        TextChannel tChannel = createChannel(event, ++channelId);
        VoiceChannel vChannel = createVoiceChannel(event, channelId);
        DiscordLobby<G> lobby = lobbies.newLobby(user,tChannel);
        lobby.setAssociatedVoiceChannel(vChannel);
        lobbies.activeEmoteListenerJoin(event.getMessage(), lobby);
    }

    private VoiceChannel createVoiceChannel(CommandEvent event, int id) {
        Guild guild = event.getGuild();
        VoiceChannel result = guild.createVoiceChannel(id+"-"+gameClass.getSimpleName().replaceAll("(.)([A-Z])", "$1-$2"))
                .setParent(event.getTextChannel().getParent())
                .complete();
        result.createPermissionOverride(guild.getPublicRole())
                .setDeny(Permission.ALL_PERMISSIONS)
                .setAllow(Permission.EMPTY_PERMISSIONS)
                .reason("Remove @everyone from the channel")
                .queue();
        return result;
    }

    private TextChannel createChannel(CommandEvent event, int id) {
        Guild guild = event.getGuild();
        TextChannel result = guild.createTextChannel(id+"-"+gameClass.getSimpleName().replaceAll("(.)([A-Z])", "$1-$2"))
                    .setParent(event.getTextChannel().getParent())
                    .setTopic(  "| " +
//                            MyEmote.EXCLAMATION.getId()+" **"+prefix+LobbyJoin.NAME+"** pour rejoindre la partie. \n" +
                            MyEmote.EXCLAMATION.getId()+" **"+prefix+StartGame.NAME+"** pour pour démarrer la partie. " +
                            MyEmote.EXCLAMATION.getId())
                    .complete();
        result.createPermissionOverride(guild.getPublicRole())
                .setDeny(Permission.ALL_PERMISSIONS)
                .setAllow(Permission.EMPTY_PERMISSIONS)
                .reason("Remove @everyone from the channel")
                .queue();
        return result;
    }
}
