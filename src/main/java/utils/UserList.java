package utils;

import discord.User;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

public class UserList {
    private LinkedList<User> list = new LinkedList<>();

    @NotNull
    public User getUser(User equivalent){
        User user = containUser(equivalent);
        if (user == null) {
            user = equivalent;
            addUser(equivalent);
        }
        return user;
    }

    private void addUser(User equivalent) {
        list.add(equivalent);
    }

    private User containUser(User equivalent) {
        for (User user : list) if(equivalent.equals(user)) return user;
        return null;
    }
}
