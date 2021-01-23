package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.utils.ColorTheme;
import com.bdj.bot_discord.discord.utils.GameDistributor;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.lobby.Game;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

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
    }

    @Override
    protected void executeAux(CommandEvent event) {
        DiscordLobby<G> lobby = lobbies.getLobby(event.getChannel());

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Lobby information", null);
        eb.setDescription("Lobby pour une partie de "+gameName);
        eb.setColor(ColorTheme.INFO.getColor());

        eb.addField("Administrateur",lobby.getAdmins().toString(), false);

        StringBuilder playerList = new StringBuilder();
        for (User user : lobby.getUsers()) playerList.append(user.toString()).append("\t|\t");
        eb.addField("Joueurs", String.valueOf(playerList), false);

        lobby.getChannel().sendMessage(eb.build()).queue();
    }
}
