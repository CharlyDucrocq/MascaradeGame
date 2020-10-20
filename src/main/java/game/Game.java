package game;

import utils.LoopList;

public class Game {
    private final int nbRoundForInit = 1;
    private LoopList<Player> players;

    Game(LoopList<Player> players) {
        this.players = players;
    }
}
