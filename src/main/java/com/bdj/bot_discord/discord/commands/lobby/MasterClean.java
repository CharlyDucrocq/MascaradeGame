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
import com.bdj.bot_discord.lobby.Lobby;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.ChannelManager;

import java.util.Collection;
import java.util.Objects;

import static com.bdj.bot_discord.main.Application.getUser;

public class MasterClean <G extends Game> extends ErrorCatcherCommand {
    private final GameDistributor<G> lobbies;

    public MasterClean(GameDistributor<G> lobbies){
        this.lobbies = lobbies;

        this.name = "clean";
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"cleanAll"};
        this.help = "Termine toutes les partie, ferme tous les lobby, supprime tous les channel.";

        this.ownerCommand=true;
    }
    @Override
    protected void executeAux(CommandEvent event) {
        this.lobbies
                .getAll()
                .forEach(lobby->{
                    if(!lobby.gameOver()) lobby.killGame();

                    lobbies.deleteLobby(lobby);
                });
    }
}
