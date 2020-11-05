package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;

public class MascaradeOut {
    private InOutGameInterface inOut;

    public MascaradeOut(InOutGameInterface inOut){
        this.inOut = inOut;
    }

    public void printStart(Game game){

    }

    public void printPeek(Player player){

    }

    public void printStartTurn(GameRound gameRound) {

    }

    public void printPublicSwitch(Player player, Player other){

    }

    public void printContest(Player opponent) {
    }

    public void printUncontest(Player opponent) {
    }

    public void printSetCharacter(GameRound gameRound) {
    }


    public void printAction(Player p, Character charaChose) {

    }

    public void printPenality(Player p) {
    }
}
