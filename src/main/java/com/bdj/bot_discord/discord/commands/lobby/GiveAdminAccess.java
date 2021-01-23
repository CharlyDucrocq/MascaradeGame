package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.commands.MyRole;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.utils.GameDistributor;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.InvalidCommand;
import com.bdj.bot_discord.errors.NoGameCreated;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.main.Application;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.List;
import java.util.stream.Collectors;

import static com.bdj.bot_discord.main.Application.getUser;

public class GiveAdminAccess<G extends Game> extends ErrorCatcherCommand {
    private final GameDistributor<G> lobbies;

    public GiveAdminAccess(GameDistributor<G> lobbies){
        this.lobbies = lobbies;

        this.name = "giveAccess";
        this.category = MyCommandCategory.GAME_GESTION;
        this.arguments="[userTag]";
        this.help = "Donne les droits d'administration sur la partie de ce channel. (Il faut être admin pour effectuer cette commande)";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<G> lobby = lobbies.getLobby(event.getChannel());
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user)) throw new BadUser();

        getUserTagged(event).forEach(lobby::addAdmin);
    }

    private List<User> getUserTagged(CommandEvent event){
        List<net.dv8tion.jda.api.entities.User> tagged = event.getMessage().getMentionedUsers();
        if (tagged.isEmpty())
            throw new InvalidCommand();
        return tagged.stream().map(Application::getUser).collect(Collectors.toList());
    }
}
