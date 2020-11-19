package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.games.mascarade.card.Character;
import com.bdj.bot_discord.games.mascarade.card.Cheater;
import com.bdj.bot_discord.utils.choice.QuestionAnswers;

import java.util.Objects;

public class Player {
    private Character currentCharacter;
    private Purse purse = new Purse();
    private User user;
    private int id = 0;

    private boolean haveWin = false;


    public Player(User user){
        this.user = user;
    }

    public Player(User user, int id){//for test
        this(user);
        this.id = id;
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
        return purse.getValue() >= GlobalParameter.GLOBAL_GOAL;
    }

    public void payPenalty(Bank bank) {
        bank.takeTaxFrom(this);
    }

    @Override
    public String toString() {
        return user.getName()+(id==0 ?"":id)+"("+purse.getValue()+"£)";
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

    public void ask(QuestionAnswers question) {
        user.ask(question);
    }
}
