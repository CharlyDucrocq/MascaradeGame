package com.bdj.bot_discord.discord.lobby;

import com.bdj.bot_discord.discord.utils.InOutDiscord;
import com.bdj.bot_discord.discord.utils.MyEmote;
import com.bdj.bot_discord.discord.utils.ReactionAnalyser;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;
import com.bdj.bot_discord.lobby.GameFactory;
import com.bdj.bot_discord.lobby.Lobby;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DiscordLobby<G extends Game> extends Lobby<G> {
    public final PrivateChannels inOut;

    public DiscordLobby(PrivateChannels inOutMock, int max){
        super(max);
        inOut = inOutMock;
        updateChannelAccess();
    }

    @Override
    public void addPlayer(User user){
        super.addPlayer(user);
        inOut.printGlobalMsg(user.toString()+" a rejoint la partie");
        if (!isAdmin(user))
            inOut.allowPermission(user);
    }

    @Override
    public void addAdmin(User user) {
        super.addAdmin(user);
        if (!isPlayer(user))
            inOut.allowPermission(user);
    }

    @Override
    public boolean removePlayer(User user) {
        if(super.removePlayer(user)){
            inOut.printGlobalMsg(user.toString()+" a quitté la partie");
            if (!isAdmin(user))
                inOut.deletePermissions(user);
            return true;
        }
        inOut.printError(new Exception(user.toString()+" n'est pas dans la partie"));
        return false;
    }

    @Override
    protected void setInOut(GameFactory<? extends G> factory) {
        factory.setDiscordOut(inOut.getGlobalChannel());
    }

    public PrivateChannels getInOut() {
        return inOut;
    }

    public MessageChannel getChannel() {
        return inOut.getGlobalChannel();
    }

    private void updateChannelAccess(){
        if (inOut.noChannel()) return;
        Set<User> users = new HashSet<>(getUsers());
        users.addAll(getAdmins());

        inOut.deleteAllMemberPermissions();
        for (User user : users){
            inOut.allowPermission(user);
        }
    }

    @Override
    public void killLobby() {
        super.killLobby();
        inOut.killChannels();
    }
}
