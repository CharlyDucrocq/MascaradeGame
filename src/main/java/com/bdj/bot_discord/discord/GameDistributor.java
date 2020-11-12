package com.bdj.bot_discord.discord;

import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.errors.GameNotFinished;
import com.bdj.bot_discord.errors.NoGameStarted;
import com.bdj.bot_discord.errors.NoLobbyFound;
import com.bdj.bot_discord.lobby.Lobby;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.HashMap;
import java.util.Map;

public class GameDistributor<G extends Game> {
    private int maxByLobby;
    private Map<User, DiscordLobby<G>> userGame = new HashMap<>();
    private Map<MessageChannel, DiscordLobby<G>> channelGame = new HashMap<>();

    public GameDistributor(int max){
        maxByLobby = max;
    }

    public DiscordLobby<G> newLobby(User admin, MessageChannel channel){
        DiscordLobby<G> prev = userGame.get(admin);
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
        DiscordLobby<G> newOne = new DiscordLobby<>(maxByLobby);
        userGame.put(admin,newOne);
        newOne.setAdmin(admin);
        associate(channel,newOne);
        newOne.getInOut().printGlobalMsg("Partie créée. Admin : "+admin.toString());
        return newOne;
    }

    private boolean channelOccupied(MessageChannel channel) {
        return channelGame.containsKey(channel);
    }

    public void joinLobby(User user, DiscordLobby<G> lobby){
        DiscordLobby<G> prev = userGame.get(user);
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
        DiscordLobby<G> lobby = userGame.get(referent);
        joinLobby(newOne, lobby);
    }

    public void quit(User user){
        DiscordLobby<G> lobby = getLobby(user);
        if(lobby == null) throw new NoLobbyFound();
        if(!lobby.gameOver()) throw new GameNotFinished();

        if(!lobby.isAdmin(user)) userGame.remove(user);
        lobby.removePlayer(user);
    }

    private void deleteLobby(DiscordLobby<G> toDelete) {
        for (User user : toDelete.getUsers()) userGame.remove(user);
        userGame.remove(toDelete.getAdmin());
        channelGame.remove(toDelete.getInOut().getGlobalChannel());
        toDelete.getInOut().printGlobalMsg("La partie à bien été supprimé");
    }

    public Game getGame(User user){
        DiscordLobby<G> lobby = userGame.get(user);
        if(lobby == null) throw new NoLobbyFound();
        Game game = lobby.getGame();
        if(game == null) throw new NoGameStarted();
        if(game.isOver()) throw new GameException("Game Ended ! Please restart the game.");
        return game;
    }

    public DiscordLobby<G> getLobby(User user) {
        DiscordLobby<G> lobby = userGame.get(user);
        if (lobby == null) throw new NoLobbyFound();
        return lobby;
    }

    public DiscordLobby<G> getLobby(MessageChannel channel){
        return channelGame.get(channel);
    }

    public void associate(MessageChannel channel, DiscordLobby<G> lobby){
        if (!lobby.getInOut().noChannel()){
            channelGame.remove(channel);
        }
        lobby.getInOut().setGlobalChannel(channel);
        channelGame.put(channel,lobby);
    }

    public boolean isInGame(User user) {
        Lobby<G> lobby = getLobby(user);
        if (lobby == null) return false;
        return !lobby.gameOver();
    }

    public boolean isInLobby(User user) {
        Lobby<G> lobby = getLobby(user);
        return lobby != null;
    }
}
