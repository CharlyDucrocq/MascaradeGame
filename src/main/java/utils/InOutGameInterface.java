package utils;

import game.Player;

public interface InOutGameInterface {
    void printGlobalMsg(String message);
    void printPersonalMsg(Player player, String message);
    void countDown(int from);
    int askChoiceTo(Player player, ChoiceDescritpion describ);
}
