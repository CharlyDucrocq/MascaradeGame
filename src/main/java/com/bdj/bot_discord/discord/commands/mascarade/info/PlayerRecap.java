package com.bdj.bot_discord.discord.commands.mascarade.info;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getMascaradeGame;
import static com.bdj.bot_discord.main.Application.getUser;

public class PlayerRecap  extends ErrorCatcherCommand {
    public PlayerRecap(){
        this.name = "playersRecap";
        this.aliases = new String[]{"recapPlayers", "players","joueurs"};
        this.help = "Affichage des joueurs en jeu et de leur situation.";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        MascaradeGame game = getMascaradeGame(user);
        game.getOut().printPlayerRecap(game.getTable().getPlayers());
    }
}
