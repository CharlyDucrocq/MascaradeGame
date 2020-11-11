package com.bdj.bot_discord.errors;

public class NoLobbyFound extends GameException{
    public NoLobbyFound(){
        super("Aucun lobby trouvé");
    }
}
