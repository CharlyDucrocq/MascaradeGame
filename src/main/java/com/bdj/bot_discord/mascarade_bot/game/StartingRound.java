package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.errors.ActionNotAllowed;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

public class StartingRound extends GameRound {
    public StartingRound(Game game, Player next) {
        super(game, next);
    }

    @Override
    public void setCharacterToUse(Character c){
        throw getError();
    }

    @Override
    public void peekCharacter(){
        throw getError();
    }

    @Override
    public void useCharacter(){
        throw getError();
    }

    @Override
    public void contest(Player opponent){
        throw getError();
    }

    private ActionNotAllowed getError(){
        return new ActionNotAllowed("Vous ne pouvez pas faire ca mtn : Vous ne pouvez qu'échanger vos cartes pour l'instant");
    }
}
