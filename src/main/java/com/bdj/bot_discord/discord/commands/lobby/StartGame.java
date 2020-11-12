package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.GameDistributor;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.NoGameCreated;
import com.bdj.bot_discord.games.mascarade.MascaradeFactory;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.GameFactory;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.lang.reflect.InvocationTargetException;

import static com.bdj.bot_discord.main.Application.getUser;
import static com.bdj.bot_discord.main.Application.mascaradeLobbies;

public class StartGame<G extends Game> extends ErrorCatcherCommand {
    private final GameDistributor<G> lobbies;
    private final Class<? extends GameFactory<G>> factoryClass;

    public StartGame(GameDistributor<G> lobbies, Class<? extends GameFactory<G>> factoryClass){
        this.factoryClass = factoryClass;
        this.lobbies = lobbies;
        this.name = "start";
        this.aliases = new String[]{"startGame","gameStart"};
        this.help = "Création d'une nouvelle partie.";
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<G> lobby = lobbies.getLobby(user);
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user)) throw new BadUser();

        try {
            lobby.createGame(factoryClass.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }

        lobby.getGame().start();
    }
}
