package com.bdj.bot_discord.games.mascarade;

import com.bdj.bot_discord.errors.ActionNotAllowed;
import com.bdj.bot_discord.games.mascarade.card.Character;

public class StartingRound extends GameRound {
    public StartingRound(MascaradeGame game, Player next) {
        super(game, next);
        actionsAvailable = new RoundAction[]{ RoundAction.SWITCH };
    }

    public RoundAction[] getActionsAvailable() {
        return new RoundAction[]{ RoundAction.SWITCH };
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
