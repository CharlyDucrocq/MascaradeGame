package game;

import static game.GlobalParameter.*;

public abstract class Card {
    final boolean mustBeIn = false;
    final int playersMinForBeIn = MIN_PLAYERS;

    final Character type;

    public Card(Character type){
        this.type = type;
    }

    public abstract void action();
}
