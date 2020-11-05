package com.bdj.bot_discord.mascarade_bot.discord;

import net.dv8tion.jda.api.entities.Member;

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
}
