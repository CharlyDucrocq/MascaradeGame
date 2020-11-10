package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.mascarade_bot.errors.BadUser;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.mascarade_bot.main.Application.getGame;
import static com.bdj.bot_discord.mascarade_bot.main.Application.getUser;

public class UseAction extends ErrorCatcherCommand {
    public UseAction(){
        this.name = "use";
        this.aliases = new String[]{"useAction", "utiliser", "pouvoir"};
        this.help = "Utilise le pouvoir de ton personnage (choisi préalablement).";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        round.useCharacter();
    }
}
