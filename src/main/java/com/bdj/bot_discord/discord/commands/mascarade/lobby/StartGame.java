package com.bdj.bot_discord.discord.commands.mascarade.lobby;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.mascarade_bot.errors.BadUser;
import com.bdj.bot_discord.mascarade_bot.errors.NoGameCreated;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeFactory;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.mascarade_bot.main.Application.getUser;
import static com.bdj.bot_discord.mascarade_bot.main.Application.mascaradeLobbies;

public class StartGame extends ErrorCatcherCommand {
    public StartGame(){
        this.name = "start";
        this.aliases = new String[]{"startGame","gameStart"};
        this.help = "Création d'une nouvelle partie.";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<MascaradeGame> lobby = mascaradeLobbies.getLobby(user);
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user)) throw new BadUser();

        lobby.createGame(new MascaradeFactory());
        lobby.getGame().start();
    }
}
