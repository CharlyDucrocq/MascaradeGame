package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.InvalidCommand;
import com.bdj.bot_discord.games.mascarade.GameRound;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.List;

import static com.bdj.bot_discord.main.Application.*;
import static com.bdj.bot_discord.main.Application.random;

public class SwitchAction extends ErrorCatcherCommand {
    public SwitchAction(){
        this.name = "switch";
        this.aliases = new String[]{"switchCard", "switchAction","echange"};
        this.arguments = "[target]";
        this.help = "Echange mon personnage (ou non) avec [target].";
        this.category = MyCommandCategory.GAME_INFO;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User userWhoAsk = getUser(event);
        MascaradeGame game = getMascaradeGame(userWhoAsk);
        GameRound round = game.getRound();

        if(!userWhoAsk.equals(round.getUser())) throw new BadUser();

        List<net.dv8tion.jda.api.entities.User> list = event.getMessage().getMentionedUsers();
        if (list.size()>=2) throw new InvalidCommand();
        Player player = game.getTable().getPlayer(new User(list.get(0)));
        if (player==null) throw new InvalidCommand();

        round.switchCard(player, random.nextBoolean());
    }
}
