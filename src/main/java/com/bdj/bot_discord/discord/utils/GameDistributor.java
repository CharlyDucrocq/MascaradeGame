package com.bdj.bot_discord.discord.utils;

import com.bdj.bot_discord.discord.lobby.DiscordLobby;
import com.bdj.bot_discord.discord.lobby.PrivateChannels;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.errors.GameException;
import com.bdj.bot_discord.errors.GameNotFinished;
import com.bdj.bot_discord.errors.NoGameStarted;
import com.bdj.bot_discord.errors.NoLobbyFound;
import com.bdj.bot_discord.lobby.Lobby;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;
import java.util.stream.Collectors;

import static com.bdj.bot_discord.main.Application.getUser;

public class GameDistributor<G extends Game> {
    private int maxByLobby;
    private Map<User, Set<DiscordLobby<G>>> adminGame = new HashMap<>();
    private Map<User, DiscordLobby<G>> userGame = new HashMap<>();
    private Map<MessageChannel, DiscordLobby<G>> channelGame = new HashMap<>();

    public GameDistributor(int max){
        maxByLobby = max;
    }

    public DiscordLobby<G> newLobby(User admin, PrivateChannels channels){
        if(channelOccupied(channels.tChannel))
            throw new GameException("Ce channel est déjà occupé");
        DiscordLobby<G> newOne = new DiscordLobby<>(channels, maxByLobby);
        adminGame.putIfAbsent(admin, new HashSet<>());
        adminGame.get(admin).add(newOne);
        newOne.addAdmin(admin);
        associate(channels.tChannel,newOne);
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

    public void deleteLobby(DiscordLobby<G> toDelete) {
        for (User user : toDelete.getUsers()) userGame.remove(user);
        toDelete.getAdmins().forEach(adminGame::remove);
        channelGame.remove(toDelete.getInOut().getGlobalChannel());
        toDelete.killLobby();
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

    public void associate(TextChannel channel, DiscordLobby<G> lobby){
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
        Lobby<G> lobby = userGame.get(user);
        return lobby != null;
    }

    public Collection<DiscordLobby<G>> getAll() {
        return adminGame.values().stream().flatMap(Set::stream).collect(Collectors.toSet());
    }

    public void registerAdmins(DiscordLobby<G> lobby){
        lobby.getAdmins().forEach(a->{
            adminGame.putIfAbsent(a,new HashSet<>());
            adminGame.get(a).add(lobby);
        });
    }
}
