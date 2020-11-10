package com.bdj.bot_discord.discord.commands.mascarade.info;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.mascarade_bot.main.Application.getGame;
import static com.bdj.bot_discord.mascarade_bot.main.Application.getUser;

public class CharacterRecap extends ErrorCatcherCommand {
    public CharacterRecap(){
        this.name = "charactersRecap";
        this.aliases = new String[]{"recapCharacters", "characters","personnages", "perso"};
        this.help = "Affichage des personnages en jeu et de leur utilité.";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        MascaradeGame game = getGame(user);
        game.getOut().printCharactersRecap(game.getCharactersList());
    }
}
