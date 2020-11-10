package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.mascarade_bot.errors.BadUser;
import com.bdj.bot_discord.mascarade_bot.errors.InvalidCommand;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.List;

import static com.bdj.bot_discord.mascarade_bot.main.Application.*;

public class PeekAction  extends ErrorCatcherCommand {
    public PeekAction(){
        this.name = "peek";
        this.aliases = new String[]{"peekCard", "peekAction","regarder"};
        this.help = "Regarder secretement mon personnage.";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        round.peekCharacter();
    }
}
