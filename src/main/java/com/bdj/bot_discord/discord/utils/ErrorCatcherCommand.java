package com.bdj.bot_discord.discord.utils;

import com.bdj.bot_discord.errors.GameException;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public abstract class ErrorCatcherCommand extends Command {

    @Override
    protected final void execute(CommandEvent commandEvent) {
        try{
            executeAux(commandEvent);
        } catch (GameException e){
            InOutDiscord.printError(e,commandEvent.getChannel());
        }
    }

    protected abstract void executeAux(CommandEvent event);
}
