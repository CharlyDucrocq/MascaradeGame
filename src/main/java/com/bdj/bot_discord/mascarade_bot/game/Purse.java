package com.bdj.bot_discord.mascarade_bot.game;

public class Purse {
    private int val = GlobalParameter.STARTING_PURSE;

    public void addCoin(int toAdd){
        val+=toAdd;
        if (val<0) val=0;
    }

    public int removeCoin(int toRemove){
        int before = val;
        val-=toRemove;
        if (val<0) val=0;
        return before-val;
    }

    public boolean isEmpty(){
        return val<=0;
    }

    public int getValue() {
        return val;
    }
}
