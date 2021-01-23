package com.bdj.bot_discord.discord.lobby;

import com.bdj.bot_discord.discord.utils.InOutDiscord;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.GameFactory;
import com.bdj.bot_discord.lobby.Lobby;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class DiscordLobby<G extends Game> extends Lobby<G> {
    final InOutDiscord inOut;
    private VoiceChannel voiceChannel;

    public DiscordLobby(int max){
        super(max);
        inOut = new InOutDiscord();
    }

    public DiscordLobby(InOutDiscord inOutMock, int max){
        super(max);
        inOut = inOutMock;
    }

    @Override
    public void addPlayer(User user){
        super.addPlayer(user);
        inOut.printGlobalMsg(user.toString()+" a rejoint la partie");
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

    public void setAssociatedVoiceChannel(VoiceChannel vChannel) {
        this.voiceChannel =vChannel;
    }

    public VoiceChannel getVoiceChannel() {
        return voiceChannel;
    }
}
