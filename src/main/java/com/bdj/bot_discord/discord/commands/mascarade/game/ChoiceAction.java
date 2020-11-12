package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.discord.commands.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.errors.BadUser;
import com.bdj.bot_discord.errors.InvalidCommand;
import com.bdj.bot_discord.games.mascarade.GameRound;
import com.bdj.bot_discord.games.mascarade.card.Character;
import com.jagrosh.jdautilities.command.CommandEvent;

import static com.bdj.bot_discord.main.Application.*;

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
        GameRound round = getMascaradeGame(user).getRound();
        if(!user.equals(round.getUser())) throw new BadUser();

        String firstParam = extractFirstParameter(event.getMessage().getContentRaw());
        Character choice = Character.getFromText(firstParam);
        if (choice==null) throw new InvalidCommand();

        round.setCharacterToUse(choice);
    }
}
