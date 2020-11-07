package com.bdj.bot_discord.mascarade_bot.errors;

public class NoLobbyFound extends GameException{
    public NoLobbyFound(){
        super("Aucun lobby trouvé");
    }
}
