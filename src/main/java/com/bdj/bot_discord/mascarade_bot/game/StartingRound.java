package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.errors.ActionNotAllowed;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;

public class StartingRound extends GameRound {
    public StartingRound(Game game, Player next) {
        super(game, next);
    }

    @Override
    public void setCharacterToUse(Character c){
        throw new ActionNotAllowed();
    }

    @Override
    public void peekCharacter(){
        throw new ActionNotAllowed();
    }

    @Override
    public void useCharacter(){
        throw new ActionNotAllowed();
    }

    @Override
    public void contest(Player opponent){
        throw new ActionNotAllowed();
    }
}
