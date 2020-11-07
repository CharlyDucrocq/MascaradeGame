package com.bdj.bot_discord.mascarade_bot.discord;

import net.dv8tion.jda.api.entities.Member;

import java.util.Objects;

public class User {
    private net.dv8tion.jda.api.entities.User user;

    public User(net.dv8tion.jda.api.entities.User user){
        this.user = user;
    }

    public String getName() {
        return user.getName();
    }

    public void sendMessage(String message) {
        user.openPrivateChannel().complete().sendMessage(message).queue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof String) {
            String name = ((String) o).toLowerCase();
            return name.equals(toString())
                    || name.equals(getName());
        }
        if (getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.user, user.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user);
    }

    @Override
    public String toString() {
        return user.getAsMention();
    }
}
