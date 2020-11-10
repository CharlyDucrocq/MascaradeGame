package com.bdj.bot_discord.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import static com.bdj.bot_discord.mascarade_bot.main.Application.*;

public class MyCommandCategory {
    public static Command.Category GAME_ACTION = new Command.Category("Action de tour de jeu", (e) -> mascaradeLobbies.isInLobby(getUser(e)));
}
