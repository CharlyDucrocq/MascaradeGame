package game.card;

import game.Player;

public abstract class Card {
    final Character type;
    final Player player;

    public Card(Character type, Player player){
        this.type = type;
        this.player = player;
    }

    public abstract void action();
}
