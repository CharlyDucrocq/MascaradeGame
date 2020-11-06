package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.Game;
import com.bdj.bot_discord.mascarade_bot.game.Player;

public enum Character implements CardCreator {
    JUDGE(
            "Juge",
            "Récupère toutes les pièces payés en amende à la banque",
            Judge::create
    ),
    BISHOP(
            "Eveque",
            "Prend "+Bishop.COIN_TO_SWITCH+" piece au membre le plus riche.",
            Bishop::create
    ),
    THIEF(
            "Voleur",
            "Vole "+Thief.COIN_STOLE+" piece(s) à ses voisins",
            Thief::create
    ),
    KING(
            "Roi",
            "Gagne "+King.COIN_ADD+" piece(s)",
            King::create
    ),
    QUEEN(
            "Reine",
            "Gagne "+Queen.COIN_ADD+" piece(s)",
            Queen::create
    ),
    WITCH(
            "Sorcière",
            //TODO a changer en même temps que la methode associé
            "Echange sa bourse avec un joueur (le plus riche - provisoire)",
            Witch::create
    ),
    BEGGAR(
            "Mendiant",
            "Ne peut rien faire si ce n'est que mendier",
            Beggar::create
    ),
    /*
    SPY,
    PEASANT,
    JOKER,
    CHEATER,
    WIDOW,
    INQUISITOR*/
    NEVER_ON_GAME() //only for test
    ;

    private String frenchName;
    private String description;
    private CardCreator creator;

    Character(){
        //only for test
        }

    Character(String nom, String describ, CardCreator creator){
        this.frenchName = nom;
        this.description = describ;
        this.creator = creator;
    }

    public static Character getFromText(String firstParam) {
        for (Character character : values()){
            if(character.equivalentTo(firstParam)) return character;
        }
        return null;
    }

    public String getDescription() {
        return description;
    }

    public Card getCard(Player player, Game game){
        return creator.getCard(player, game);
    }

    @Override
    public String toString() {
        return frenchName;
    }

    public boolean equivalentTo(String txt){
        txt = txt.toLowerCase();
        return txt.equals(frenchName) || txt.equals(name().toLowerCase());
    }
}
