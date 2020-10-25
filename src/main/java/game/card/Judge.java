package game.card;

import game.Banque;
import game.Player;

public class Judge extends Card {
    Banque banque;

    public Judge(Player player, Banque banque) {
        super(Character.JUDGE,player);
        this.banque = banque;
    }

    @Override
    public void action() {
        banque.reverse(player);
    }
}
