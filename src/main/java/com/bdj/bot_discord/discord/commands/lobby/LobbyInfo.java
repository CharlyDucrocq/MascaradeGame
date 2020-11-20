package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.ColorTheme;
import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import static com.bdj.bot_discord.main.Application.*;

public class LobbyInfo<G extends Game> extends ErrorCatcherCommand {
    private final String gameName;
    private final GameDistributor<G> lobbies;

    public LobbyInfo(GameDistributor<G> lobbies, String gameName){
        this.gameName = gameName;
        this.lobbies = lobbies;

        this.name = "lobby";
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"lobbyInfo","infoLobby","listPlayer"};
        this.help = "Information sur le lobby courant.";
        this.ownerCommand = true;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        DiscordLobby<G> lobby = lobbies.getLobby(getUser(event));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Lobby information", null);
        eb.setDescription("Lobby pour une partie de "+gameName);
        eb.setColor(ColorTheme.INFO.getColor());

        eb.addField("Administrateur",lobby.getAdmin().toString(), false);

        StringBuilder playerList = new StringBuilder();
        for (User user : lobby.getUsers()) playerList.append(user.toString()).append("\t|\t");
        eb.addField("Joueurs", String.valueOf(playerList), false);

        lobby.getChannel().sendMessage(eb.build()).queue();
    }
}
