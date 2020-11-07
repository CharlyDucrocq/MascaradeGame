package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.game.card.Cheater;

import java.util.Objects;

public class Player {
    private Character currentCharacter;
    private Purse purse = new Purse();
    private User user;

    boolean haveWin = false;


    Player(User user){
        this.user = user;
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
        if(haveWin) return true;
        if (purse.isEmpty()) return true;
        if (purse.getValue()>=GlobalParameter.GLOBAL_GOAL) return true;
        return false;
    }

    public void payPenalty(Bank bank) {
        bank.takeTaxFrom(this);
    }

    @Override
    public String toString() {
        return user.getName()+"("+purse.getValue()+"£)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o instanceof String) return user.equals(o);
        if (o instanceof User) return user.equals(o);
        if (getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return currentCharacter == player.currentCharacter &&
                Objects.equals(purse, player.purse) &&
                Objects.equals(user, player.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentCharacter, purse, user);
    }

    public void cheat() {
        if(purse.getValue()>= Cheater.GOAL_FOR_WIN) haveWin = true;
    }
}
