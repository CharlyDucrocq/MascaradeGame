package com.bdj.bot_discord.mascarade_bot.discord;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class User {
    private Member member;

    public User(Member member){
        this.member = member;
    }

    public String getName() {
        return member.getNickname();
    }

    public void sendMessage(String message) {
        member.getDefaultChannel().sendMessage(message);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof String) {
            String name = ((String) o).toLowerCase();
            return (member.getNickname() !=null && name.equals(member.getNickname().toLowerCase()))
                    || name.equals(member.getEffectiveName().toLowerCase());
        }
        if (getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(member, user.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }
}
