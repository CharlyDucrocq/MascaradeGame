package com.bdj.bot_discord.mascarade_bot.game;

public class Banque {
    int stock = 0;

    public void give(int gift){
        stock+=gift;
    }

    public void reverse(Player player){
        //TODO
    }
}
