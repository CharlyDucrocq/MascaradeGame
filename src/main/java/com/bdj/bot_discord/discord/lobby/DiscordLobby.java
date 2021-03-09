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
    final InOutDiscord inOut;
    private VoiceChannel voiceChannel;

    public DiscordLobby(int max){
        super(max);
        inOut = new InOutDiscord();
    }

    public DiscordLobby(InOutDiscord inOutMock, int max){
        super(max);
        inOut = inOutMock;
        updateChannelAccess();
    }

    @Override
    public void addPlayer(User user){
        super.addPlayer(user);
        inOut.printGlobalMsg(user.toString()+" a rejoint la partie");
        if (!inOut.noChannel() && !isAdmin(user))
            PermissionLg.allowPermission(inOut.getGlobalChannel(), inOut.getGlobalChannel().getGuild().getMemberByTag(user.getDiscordUser().getAsTag()));
    }

    @Override
    public void addAdmin(User user) {
        super.addAdmin(user);
        if (!inOut.noChannel() && !isPlayer(user))
            PermissionLg.allowPermission(inOut.getGlobalChannel(), inOut.getGlobalChannel().getGuild().getMemberByTag(user.getDiscordUser().getAsTag()));
    }

    @Override
    public boolean removePlayer(User user) {
        if(super.removePlayer(user)){
            inOut.printGlobalMsg(user.toString()+" a quitté la partie");
            if (!inOut.noChannel() && !isAdmin(user))
                PermissionLg.deletePermissions(inOut.getGlobalChannel(), inOut.getGlobalChannel().getGuild().getMemberByTag(user.getDiscordUser().getAsTag()));
            return true;
        }
        inOut.printError(new Exception(user.toString()+" n'est pas dans la partie"));
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

    private void updateChannelAccess(){
        if (inOut.noChannel()) return;
        Set<User> users = new HashSet<>(getUsers());
        users.addAll(getAdmins());
        TextChannel channel = inOut.getGlobalChannel();

        PermissionLg.deleteAllMemberPermissions(channel);
        for (User user : users){
            PermissionLg.allowPermission(channel, channel.getGuild().getMemberByTag(user.getDiscordUser().getAsTag()));
        }
    }

    private ReactionAnalyser joinListener;
    public void setAnalyser(ReactionAnalyser joinListener) {
        this.joinListener = joinListener;
    }

    @Override
    public void killLobby() {
        super.killLobby();
        MessageChannel channel = getChannel();
        Objects.requireNonNull(channel.getJDA().getGuildChannelById(channel.getId())).delete().queue();

        VoiceChannel vChannel = getVoiceChannel();
        if(vChannel!=null) Objects.requireNonNull(vChannel.getJDA().getGuildChannelById(vChannel.getId())).delete().queue();

        if (joinListener!=null){
            joinListener.killAll();
            joinListener=null;
        }
    }

    public static class PermissionLg {

        public static final long ROLE_PERM = Permission.getRaw(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY);

        public static void allowPermission(TextChannel channel, Member member) {
            if (channel == null) return;
            PermissionOverrideAction poa = channel.putPermissionOverride(member);
            poa.setAllow(ROLE_PERM).queue();
        }

        public static void deleteAllMemberPermissions(TextChannel channel) {
            if (channel == null) return;
            List<PermissionOverride> perms = channel.getMemberPermissionOverrides();
            for (PermissionOverride perm : perms) {
                perm.delete().queue();
            }
        }

        public static void deletePermissions(TextChannel channel, Member member) {
            if (channel == null) return;
            List<PermissionOverride> perms = channel.getMemberPermissionOverrides();
            for (PermissionOverride perm : perms) {
                if (Objects.equals(perm.getMember(), member)) {
                    perm.delete().queue();
                }
            }
        }
    }
}
