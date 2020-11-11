package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.NotInTheGame;
import com.bdj.bot_discord.games.mascarade.GameRound;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getGame;
import static com.bdj.bot_discord.main.Application.getUser;

public class Contest extends ErrorCatcherCommand {
    public Contest(){
        this.name = "contest";
        this.aliases = new String[]{"contester", "objection"};
        this.help = "Conteste le pouvoir choisis par celui dont c'est le tour (avant utilisation).";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        MascaradeGame game = getGame(user);
        GameRound round = game.getRound();

        if(user.equals(round.getUser())) throw new BadUser();

        Player player = game.getPlayer(user);
        if(player == null) throw new NotInTheGame();

        round.contest(player);
    }
}
