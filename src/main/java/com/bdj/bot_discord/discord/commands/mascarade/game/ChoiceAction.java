package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.mascarade_bot.errors.BadUser;
import com.bdj.bot_discord.mascarade_bot.errors.InvalidCommand;
import com.bdj.bot_discord.mascarade_bot.game.GameRound;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeGame;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.jagrosh.jdautilities.command.CommandEvent;

import java.util.List;

import static com.bdj.bot_discord.mascarade_bot.main.Application.*;

public class ChoiceAction extends ErrorCatcherCommand {
    public ChoiceAction(){
        this.name = "choice";
        this.aliases = new String[]{"choiceAction", "choix", "choisir"};
        this.arguments = "[character]";
        this.help = "Choisis le personnage [character] pour utiliser son pouvoir.";
        this.category = MyCommandCategory.GAME_ACTION;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = getUser(event);
        GameRound round = getGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        String firstParam = extractFirstParameter(event.getMessage().getContentRaw());
        Character choice = Character.getFromText(firstParam);
        if (choice==null) throw new InvalidCommand();

        round.setCharacterToUse(choice);
    }
}
