package com.bdj.bot_discord.discord.commands;

import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.games.mascarade.MascaradeFactory;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.main.Application;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TestCommand extends ErrorCatcherCommand {

    public TestCommand(){
        this.name = "test";
        this.ownerCommand = true;
        this.guildOnly = false;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        User user = Application.getUser(event);

        //  TEST FOR CODE NAME
//        event.getChannel().sendMessage(new DiscordCodeNameTable(new Random().nextBoolean()).getEmbedWithoutReveled()).queue();
//        event.getChannel().sendMessage(new DiscordCodeNameTable(new Random().nextBoolean()).getEmbedWithReveled()).queue();

        //  TEST FOR TIMES BOMB
//        int NB_PLAYER = 4;
//        Player[] players = new Player[NB_PLAYER];
//        for (int i = 0;i<NB_PLAYER;i++) players[i]=new Player(user, i);
//        TimesBombGame game = new TimesBombGame(players,1,2);
//        game.setOut(new TimesBombOut(event.getChannel()));
//        game.start();

        // TEST FOR MASCARADE
        int NB_PLAYER = 13;
        Player[] players = new Player[NB_PLAYER];
        for (int i = 0;i<NB_PLAYER;i++) players[i]=new Player(user, i);
        MascaradeFactory factory = new MascaradeFactory();
        factory.setPlayers(players);
        factory.setDiscordOut(event.getChannel());
        MascaradeGame game = factory.createGame();
        game.disableStartingTurn();
        game.start();
    }
}
