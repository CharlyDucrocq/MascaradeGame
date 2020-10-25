package discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public enum Command {
    CREATE(CommandAction::create,"create", "createGame"),
    JOIN(CommandAction::join,"join", "joinGame"),
    START(CommandAction::start,"start", "startGame"),
    PEEK(CommandAction::peekAction,"peek", "peekMyCard"),
    SWITCH(CommandAction::switchAction,"switch", "switchCard"),
    USE(CommandAction::characterAction,"use", "useCharacter"),
    CONTEST(CommandAction::contestAction,"contest", "contestUse")
    ;

    public final String[] eventCommands;
    public final EventAction action;

    Command(EventAction action, String... eventCommands){
        this.eventCommands = eventCommands;
        this.action = action;
    }

    public String[] getEventCommands() {
        return eventCommands;
    }

    public boolean isUsedIn(String msg) {
        for (String command : eventCommands) if (msg.contains("!"+command)) return true;
        return false;
    }

    public void doCommand(MessageReceivedEvent event) {
        action.action(event);
    }
}
