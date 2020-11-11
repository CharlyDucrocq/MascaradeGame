package com.bdj.bot_discord.discord.lobby;

import com.bdj.bot_discord.discord.InOutDiscord;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.GameFactory;
import com.bdj.bot_discord.lobby.Lobby;
import net.dv8tion.jda.api.entities.MessageChannel;

public class DiscordLobby<G extends Game> extends Lobby<G> {
    final InOutDiscord inOut;

    public DiscordLobby(){
        inOut = new InOutDiscord();
    }

    public DiscordLobby(InOutDiscord inOutMock) {
        inOut = inOutMock;
    }

    @Override
    public void addPlayer(User user){
        super.addPlayer(user);
        inOut.printGlobalMsg(user.toString()+" a rejoin la partie");
    }

    @Override
    public boolean removePlayer(User user) {
        if(super.removePlayer(user)){
            inOut.printGlobalMsg(user.toString()+" a quitté la partie");
            return true;
        }
        inOut.printError(new Exception(user.toString()+" a quitté la partie"));
        return false;
    }

    @Override
    protected void setInOut(GameFactory<? extends G> factory) {
        factory.setDiscordOut(inOut.getGlobalChannel());
    }

    public InOutDiscord getInOut() {
        return inOut;
    }

    public MessageChannel getChannel() {
        return inOut.getGlobalChannel();
    }
}
