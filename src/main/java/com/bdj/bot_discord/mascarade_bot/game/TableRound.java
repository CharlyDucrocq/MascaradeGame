package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.discord.User;

public class TableRound {
    private Player[] players;
    private int current = -1;

    int count = 0;

    public TableRound(Player[] players){
        this.players = players;
    }

    public Player next(){
        count++;
        current++;
        if(current>=players.length) current-=players.length;
        return players[current];
    }

    public Player prev(){
        count--;
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

    public int getNbTurnDone() {
        return count;
    }

    public Player getPrevOf(Player player) {
        if(player == players[0]) return players[players.length-1];
        for (int i=1;i<players.length;i++) if (player == players[i]) return players[i-1];
        throw new RuntimeException("Player Not Found");
    }

    public Player getNextOf(Player player) {
        if(player == players[players.length-1]) return players[0];
        for (int i=players.length-2;i>=0;i--) if (player == players[i]) return players[i+1];
        throw new RuntimeException("Player Not Found");
    }

    public Player getRichest(Player except) {
        int max = 0;
        Player richest = null;
        for(Player p : players) {
            if(p!=except) {
                if (p.getPurse().getValue()>max){
                    max = p.getPurse().getValue();
                    richest = p;
                }
            }
        }
        if (richest==null)throw new RuntimeException("Richest not found");
        return richest;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Player getPlayerByName(String name) {
        for (Player player : players)
            if (player.equals(name))
                return player;
        return null;
    }
}
