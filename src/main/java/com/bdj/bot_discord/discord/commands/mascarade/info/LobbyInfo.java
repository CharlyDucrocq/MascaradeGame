package com.bdj.bot_discord.discord.commands.mascarade.info;

import com.bdj.bot_discord.discord.ColorTheme;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.lobby.Lobby;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

import static com.bdj.bot_discord.mascarade_bot.main.Application.*;

public class LobbyInfo extends ErrorCatcherCommand {
    public LobbyInfo(){
        this.name = "lobby";
        this.aliases = new String[]{"lobbyInfo","infoLobby","listPlayer"};
        this.help = "Information sur le lobby courant.";
        this.ownerCommand = true;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        DiscordLobby<MascaradeGame> lobby = mascaradeLobbies.getLobby(getUser(event));

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Lobby information", null);
        eb.setDescription("Lobby pour une partie de Mascarade");
        eb.setColor(ColorTheme.INFO.getColor());

        eb.addField("Administrateur",lobby.getAdmin().toString(), false);

        StringBuilder playerList = new StringBuilder();
        for (User user : lobby.getUsers()) playerList.append(user.toString()).append("\t|\t");
        eb.addField("Joueurs", String.valueOf(playerList), false);

        lobby.getChannel().sendMessage(eb.build()).queue();
    }
}
