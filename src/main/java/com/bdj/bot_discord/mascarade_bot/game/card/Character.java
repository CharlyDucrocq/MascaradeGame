package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
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
            "Sorciere",
            "Echange sa bourse avec un joueur de votre choix",
            Witch::create
    ),
    BEGGAR(
            "Mendiant",
            "Ne peut rien faire si ce n'est que mendier",
            Beggar::create
    ),
    PEASANT(
            "Paysan",
            "Si un autre paysan s'est annoncé, gagne 2 piece, sinon 1",
            Peasant::create
    ),
    CHEATER(
            "Tricheur",
            "Si le joueur a 10 pieces ou plus, il gagne la partie et y met fin",
            Cheater::create
    ),
    SPY(
            "Espionne",
            "Regarde sa carte et celle d'un joueur de son choix et les échange (ou pas)",
            Spy::create
    ),
    JOKER(
            "Fou",
            "Echange (ou pas) deux cartes du jeu",
            Joker::create
    ),
    WIDOW(
            "Veuve",
            "Gagne autant de piece que necessaire pour arriver à 10",
            Widow::create
    ),
    INQUISITOR(
            "Inquisiteur",
            "Demande à un joueur de deviner sa carte. Après avoir été révélé, si il avait juste, rien ne se passe, sinon il donne 4 piece à l'inquisiteur.",
            Inquisitor::new
    ),
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

    public Card getCard(Player player, MascaradeGame game){
        return creator.getCard(player, game);
    }

    @Override
    public String toString() {
        return frenchName;
    }

    public boolean equivalentTo(String txt){
        txt = txt.toLowerCase();
        return txt.equals(frenchName.toLowerCase()) || txt.equals(name().toLowerCase());
    }

    public String getIconUrl(){
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/character_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }
}
