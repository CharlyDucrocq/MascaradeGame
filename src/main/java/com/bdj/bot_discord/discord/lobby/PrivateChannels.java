package com.bdj.bot_discord.discord.lobby;

import com.bdj.bot_discord.discord.commands.lobby.StartGame;
import com.bdj.bot_discord.discord.utils.InOutDiscord;
import com.bdj.bot_discord.discord.utils.MyEmote;
import com.bdj.bot_discord.discord.utils.ReactionAnalyser;
import com.bdj.bot_discord.discord.utils.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;

import java.util.List;
import java.util.Objects;

public class PrivateChannels extends InOutDiscord {
    public static final long ROLE_PERM = Permission.getRaw(Permission.MESSAGE_READ, Permission.MESSAGE_WRITE, Permission.MESSAGE_HISTORY);
    public final TextChannel tChannel;
    public final VoiceChannel vChannel;

    private boolean dynamicAccessActivated = false;

    public PrivateChannels(Guild guild, String name, Category category, boolean dynamicAccess){
        this(
                createTextChannels(guild, name, category, dynamicAccess),
                createVoiceChannels(guild, name, category, dynamicAccess),
                dynamicAccess
        );
    }

    public PrivateChannels(TextChannel tChannel, VoiceChannel vChannel, boolean dynamicAccess){
        this.dynamicAccessActivated = dynamicAccess;
        this.tChannel = tChannel;
        this.vChannel = vChannel;
        setGlobalChannel(tChannel);
    }

    public void allowPermission(User user) {
        if (!dynamicAccessActivated) return;
        Member member = userToMember(user);
        PermissionOverrideAction poa =
                (tChannel.getPermissionOverride(member)==null) ?
                        tChannel.createPermissionOverride(member) :
                        tChannel.putPermissionOverride(member);
        poa.setAllow(ROLE_PERM).queue();
    }

    public void deleteAllMemberPermissions() {
        if (!dynamicAccessActivated) return;
        List<PermissionOverride> perms = tChannel.getMemberPermissionOverrides();
        for (PermissionOverride perm : perms) {
            perm.delete().queue();
        }
    }

    public void deletePermissions(User user) {
        if (tChannel == null || !dynamicAccessActivated) return;
        List<PermissionOverride> perms = tChannel.getMemberPermissionOverrides();
        for (PermissionOverride perm : perms) {
            if (Objects.equals(perm.getMember(), userToMember(user))) {
                perm.delete().queue();
            }
        }
    }

    private Member userToMember(User user){
        return tChannel.getGuild()
                .getMemberById(
                        user.getDiscordUser().getId()
                );
    }

    private static TextChannel createTextChannels(Guild guild, String name, Category category, boolean dynamicAccess) {
        TextChannel result = guild.createTextChannel(name)
                .setParent(category)
//                .setTopic(  "| " +
//                        MyEmote.EXCLAMATION.getId()+" **"+prefix+LobbyJoin.NAME+"** pour rejoindre la partie. \n" +
//                        MyEmote.EXCLAMATION.getId()+" **"+prefix+ StartGame.NAME+"** pour pour démarrer la partie. " +
//                        MyEmote.EXCLAMATION.getId())
                .complete();
        if(dynamicAccess)
            result.createPermissionOverride(guild.getPublicRole())
                    .setDeny(Permission.ALL_PERMISSIONS)
                    .setAllow(Permission.EMPTY_PERMISSIONS)
                    .reason("Remove @everyone from the channel")
                    .complete();
        return result;
    }

    private static VoiceChannel createVoiceChannels(Guild guild, String name, Category category, boolean dynamicAccess) {
        VoiceChannel result = guild.createVoiceChannel(name)
                .setParent(category)
                .complete();
        if(dynamicAccess)
            result.createPermissionOverride(guild.getPublicRole())
                    .setDeny(Permission.ALL_PERMISSIONS)
                    .setAllow(Permission.EMPTY_PERMISSIONS)
                    .reason("Remove @everyone from the channel")
                    .complete();
        return result;
    }

    private ReactionAnalyser joinListener;
    public void setAnalyser(ReactionAnalyser joinListener) {
        this.joinListener = joinListener;
    }

    public void killChannels() {
        if(dynamicAccessActivated){
            Objects.requireNonNull(tChannel.getJDA().getGuildChannelById(tChannel.getId())).delete().queue();
            Objects.requireNonNull(vChannel.getJDA().getGuildChannelById(vChannel.getId())).delete().queue();
        }

        if (joinListener!=null){
            joinListener.killAll();
            joinListener=null;
        }
    }
}
