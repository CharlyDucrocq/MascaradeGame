package com.bdj.bot_discord.games.mascarade;

import java.util.function.Consumer;

public enum RoundAction {
    PEEK("Voir sa carte", GameRound::peekCharacter),
    SWITCH("Echanger (ou pas) sa carte avec un autre",GameRound::askForSwitch),
    USE("Utiliser un pouvoir", GameRound::askForUse),
    ;

    public static String penalityIconUrl = "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/action_icon/penality_icon.png?raw=true";

    private final String description;
    private final Consumer<GameRound> consumer;

    RoundAction(String description, Consumer<GameRound> action){
        this.description = description;
        this.consumer = action;
    }

    public String getDescription() {
        return description;
    }

    public void doAction(GameRound round){
        consumer.accept(round);
    }

    public String getIconUrl(){
        return "https://github.com/CharlyDucrocq/MascaradeGame/blob/main/rsrc/img/action_icon/"+name().toLowerCase()+"_icon.png?raw=true";
    }
}
