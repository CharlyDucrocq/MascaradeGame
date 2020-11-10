package com.bdj.bot_discord.discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public enum Command {
    CREATE(CommandAction::create,"Créer une nouvelle partie","create", "createGame"),
    JOIN(CommandAction::join,"Rejoindre une nouvelle partie","join", "joinGame"),
    START(CommandAction::start,"Commencer la partie","start", "startGame"),
    PEEK(CommandAction::peekAction,"Action de tour : Voir sa carte","peek", "peekMyCard"),
    SWITCH(CommandAction::switchAction,"Action de tour : Echanger sa carte avec un joueur (Exemple: !switch nomDuJoueur)","switch", "switchCard"),
    CHOICE(CommandAction::characterChoice, "Action de tour (en pair avec use) : Choisir un pouvoir a activé (Exemple : !choice roi)","choice","choiceCharacter"),
    USE(CommandAction::characterAction,"Action de tour (en paire avec choice) : Utiliser le pouvoir (10s minimum entre les deux commandes)","use", "useCharacter"),
    CONTEST(CommandAction::contestAction,"Contester la carte annoncer par le joueur (a utilisé entre choice et use)","contest", "objection"),
    PLAYER_RECAP(CommandAction::recapPlayer,"Aide : Recap des Joueur et de leur bourse","gameRecap", "playerRecap"),
    CHARACTER_RECAP(CommandAction::recapCharacter,"Aide : Recap des Personnage dans le jeu","characterRecap", "actionRecap"),
    HELP(CommandAction::helpMsg,"Affichage des commandes","help","commands"),
    ;

    public final String[] eventCommands;
    public final EventAction action;
    public final String description;

    Command(EventAction action, String describ, String... eventCommands){
        this.description = describ;
        this.eventCommands = eventCommands;
        this.action = action;
    }

    public String[] getEventCommands() {
        return eventCommands;
    }

    public boolean isUsedIn(String msg) {
        for (String command : eventCommands) if (msg.contains("!"+command.toLowerCase())) return true;
        return false;
    }

    public void doCommand(MessageReceivedEvent event) {
        action.action(event);
    }

    public String getDescription(){
        return description;
    }
}
