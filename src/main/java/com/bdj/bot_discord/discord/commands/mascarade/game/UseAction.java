package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.games.mascarade.GameRound;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.getMascaradeGame;
import static com.bdj.bot_discord.main.Application.getUser;

public class UseAction extends ErrorCatcherCommand {
    public UseAction(){
        this.name = "use";
        this.aliases = new String[]{"useAction", "utiliser", "pouvoir"};
        this.help = "Utilise le pouvoir de ton personnage (choisi préalablement).";
        this.category = MyCommandCategory.GAME_INFO;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        GameRound round = getMascaradeGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();
        round.useCharacter();
    }
}
