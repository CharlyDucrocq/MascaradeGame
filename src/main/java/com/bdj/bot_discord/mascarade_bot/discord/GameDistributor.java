package com.bdj.bot_discord.mascarade_bot.discord;

import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import com.bdj.bot_discord.mascarade_bot.errors.GameNotFinished;
import com.bdj.bot_discord.mascarade_bot.errors.NoGameStarted;
import com.bdj.bot_discord.mascarade_bot.errors.NoLobbyFound;
import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.Lobby;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public class GameDistributor {
    private Map<User, Lobby> userGame = new HashMap<>();
    private Map<MessageChannel, Lobby> channelGame = new HashMap<>();

    public Lobby newLobby(User admin, MessageChannel channel){
        Lobby prev = userGame.get(admin);
        if(prev != null){
            if(prev.gameOver()){
                if(prev.isAdmin(admin))
                    deleteLobby(prev);
                else
                    quit(admin);
            } else {
                throw new GameNotFinished();
            }
        }
        if(channelOccupied(channel))
            throw new GameException("Ce channel est déjà occupé");
        Lobby newOne = new Lobby();
        userGame.put(admin,newOne);
        newOne.setAdmin(admin);
        associate(channel,newOne);
        newOne.getInOut().printGlobalMsg("Partie créée. Admin : "+admin.toString());
        return newOne;
    }

    private boolean channelOccupied(MessageChannel channel) {
        return channelGame.containsKey(channel);
    }

    public void joinLobby(User user, Lobby lobby){
        Lobby prev = userGame.get(user);
        if(prev != null && prev.isPlayer(user)){
            quit(user);
            if(prev.equals(lobby)) return;
        }
        if(lobby == null) throw new NoLobbyFound();
        lobby.addPlayer(user);
        userGame.put(user,lobby);
    }

    public void joinLobby(User user, MessageChannel channel){
        joinLobby(user, channelGame.get(channel));
    }

    public void joinLobbyOf(User newOne, User referent){
        Lobby lobby = userGame.get(referent);
        joinLobby(newOne, lobby);
    }

    public void quit(User user){
        Lobby lobby = getLobby(user);
        if(lobby == null) throw new NoLobbyFound();
        if(!lobby.gameOver()) throw new GameNotFinished();

        if(!lobby.isAdmin(user)) userGame.remove(user);
        lobby.removePlayer(user);
    }

    private void deleteLobby(Lobby toDelete) {
        for (User user : toDelete.getUsers()) userGame.remove(user);
        userGame.remove(toDelete.getAdmin());
        toDelete.getInOut().printGlobalMsg("La partie à bien été supprimé");
    }

    public Game getGame(User user){
        Lobby lobby = userGame.get(user);
        if(lobby == null) throw new NoLobbyFound();
        Game game = lobby.getGame();
        if(game == null) throw new NoGameStarted();
        if(game.isOver()) throw new GameException("Game Ended ! Please restart the game.");
        return game;
    }

    public Lobby getLobby(User user) {
        return userGame.get(user);
    }

    public Lobby getLobby(MessageChannel channel){
        return channelGame.get(channel);
    }

    public void associate(MessageChannel channel, Lobby lobby){
        if (!lobby.getInOut().noChannel()){
            channelGame.remove(channel);
        }
        lobby.getInOut().setGlobalChannel(channel);
        channelGame.put(channel,lobby);
    }
}
