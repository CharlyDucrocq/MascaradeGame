package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

public class Player {
    private Character currentCharacter;
    private Purse purse = new Purse();
    private User user;


    Player(User user){
        this.user = user;
        //TODO
    }

    public void setCurrentCharacter(Character c) {
        currentCharacter = c;
    }

    public Character getCurrentCharacter() {
        return currentCharacter;
    }

    public User getUser() {
        return user;
    }

    public Purse getPurse() {
        return purse;
    }

    public void setPurse(Purse purse) {
        this.purse = purse;
    }

    public boolean endTheGame() {
        if (purse.isEmpty()) return true;
        if (purse.getValue()>=GlobalParameter.GLOBAL_GOAL) return true;
        return false;
    }

    public void payPenalty(Bank bank) {
        bank.give(purse.removeCoin(-1));
    }

    @Override
    public String toString() {
        return user.getName()+"("+purse.getValue()+"£)";
    }
}
