package com.bdj.bot_discord.mascarade_bot.game;

public class Bank {
    private int stock = 0;

    public void give(int gift){
        stock+=gift;
    }

    public void reverse(Player player){
        player.getPurse().addCoin(stock);
        stock = 0;
    }
}
