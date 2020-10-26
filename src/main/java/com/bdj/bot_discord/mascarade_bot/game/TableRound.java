package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;

public class TableRound {
    private Player[] players;
    private int current = 0;

    public TableRound(Player[] players){
        this.players = players;
    }

    public Player next(){
        current++;
        if(current>=players.length) current-=players.length;
        return players[current];
    }

    public Player prev(){
        current--;
        if(current<0) current+=players.length;
        return players[current];
    }

    public Player getPrev(){
        int current = this.current;
        current--;
        if(current<0) current+=players.length;
        return players[current];
    }

    public Player getNext(){
        int current = this.current;
        current++;
        if(current>=players.length) current-=players.length;
        return players[current];
    }

    public Player getPlayer(User user) {
        for (Player player : players) if (user.equals(player.getUser())) return player;
        return null;
    }

    public User[] getUsers() {
        User[] users = new User[players.length];
        for(int i=0;i<users.length;i++) users[i]=players[i].getUser();
        return users;
    }
}
