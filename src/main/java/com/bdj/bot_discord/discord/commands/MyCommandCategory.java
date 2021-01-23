package com.bdj.bot_discord.discord.commands;

import com.jagrosh.jdautilities.command.Command;
import static com.bdj.bot_discord.main.Application.*;

public class MyCommandCategory {
    public static Command.Category GAME_GESTION = new Command.Category("Gestion de la partie");
    public static Command.Category GAME_INFO = new Command.Category("Info jeu", (e) -> mascaradeLobbies.isInLobby(getUser(e)));
    public static Command.Category GAME_RULES = new Command.Category("Règles du jeu");
}
