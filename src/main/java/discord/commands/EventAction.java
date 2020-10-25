package discord.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface EventAction {
    void action(MessageReceivedEvent event);
}
