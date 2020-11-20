package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.games.mascarade.GameRound;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.*;

public class PeekAction  extends ErrorCatcherCommand {
    public PeekAction(){
        this.name = "peek";
        this.aliases = new String[]{"peekCard", "peekAction","regarder"};
        this.help = "Regarder secretement mon personnage.";
        this.category = MyCommandCategory.GAME_INFO;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        GameRound round = getMascaradeGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        round.peekCharacter();
    }
}
