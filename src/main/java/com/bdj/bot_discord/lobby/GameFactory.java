package com.bdj.bot_discord.lobby;

import com.bdj.bot_discord.discord.User;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Collection;

public interface GameFactory<G extends Game> {
    void setPlayers(Collection<User> users);
    boolean haveEnoughPlayer();
    G createGame();
    void setDiscordOut(MessageChannel channel);
}
