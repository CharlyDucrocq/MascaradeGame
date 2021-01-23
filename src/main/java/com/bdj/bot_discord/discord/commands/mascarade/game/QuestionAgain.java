package com.bdj.bot_discord.discord.commands.mascarade.game;

import com.bdj.bot_discord.discord.commands.MyCommandCategory;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.main.Application;
import com.jagrosh.jdautilities.command.CommandEvent;

public class QuestionAgain extends ErrorCatcherCommand {
    public static MascaradeGame gameForTest;

    public QuestionAgain(){
        this.name = "rollback";
        this.aliases = new String[]{"question"};
        this.help = "Permet de relancer la dernière question. (en cas de bug)";
        //this.category = MyCommandCategory.GAME_INFO;
        this.ownerCommand = true;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        MascaradeGame game;
        if(gameForTest ==null) game=Application.mascaradeLobbies.getLobby(event.getChannel()).getGame();
        else game = gameForTest;
        game.getOut().reRunLastQuestion();
    }
}
