package com.bdj.bot_discord.discord.commands.lobby;

import com.bdj.bot_discord.discord.commands.MyRole;
import com.bdj.bot_discord.discord.utils.GameDistributor;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.errors.NoGameCreated;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.GameFactory;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getUser;

public class StartGame<G extends Game> extends ErrorCatcherCommand {
    public final static String NAME = "start";
    private final GameDistributor<G> lobbies;
    private final Class<? extends GameFactory<G>> factoryClass;

    public StartGame(GameDistributor<G> lobbies, Class<? extends GameFactory<G>> factoryClass){
        this.factoryClass = factoryClass;
        this.lobbies = lobbies;

        this.name = NAME;
        this.category = MyCommandCategory.GAME_GESTION;
        this.aliases = new String[]{"startGame","gameStart"};
        this.help = "Début de la partie. (Vous devez être admin pour lancer la partie)";
//        this.requiredRole= MyRole.GAME_MASTER.toString();
    }
    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        DiscordLobby<G> lobby = lobbies.getLobby(event.getChannel());
        if(lobby == null) throw new NoGameCreated();
        if(!lobby.isAdmin(user)) throw new BadUser();

        try {
            if(lobby.getGame()!=null) lobby.killGame();
            lobby.createGame(factoryClass.getDeclaredConstructor().newInstance());
            lobby.getGame().start();
        } catch (GameException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
