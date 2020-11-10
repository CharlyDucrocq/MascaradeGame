package com.bdj.bot_discord.discord.commands.mascarade.info;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.mascarade_bot.errors.BadUser;
import com.bdj.bot_discord.mascarade_bot.errors.NotInTheGame;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.mascarade_bot.main.Application.getGame;
import static com.bdj.bot_discord.mascarade_bot.main.Application.getUser;

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
        MascaradeGame game = getGame(user);
        game.getOut().printPlayerRecap(game.getTable().getPlayers());
    }
}
