package com.bdj.bot_discord.discord.commands;

import com.bdj.bot_discord.discord.commands.mascarade.game.QuestionAgain;
import com.bdj.bot_discord.discord.utils.ErrorCatcherCommand;
import com.bdj.bot_discord.discord.utils.QuestionSender;
import com.bdj.bot_discord.discord.utils.User;
//import com.bdj.bot_discord.games.mascarade.*;
import com.bdj.bot_discord.games.times_bomb.*;
import com.bdj.bot_discord.main.Application;
import com.bdj.bot_discord.utils.MyFuture;
import com.bdj.bot_discord.utils.choice.ArraysChoice;
import com.jagrosh.jdautilities.command.CommandEvent;

public class TestCommand extends ErrorCatcherCommand {

    public TestCommand(){
        this.name = "test";
        this.ownerCommand = true;
        this.guildOnly = false;
    }

    boolean verif = true;
    TimesBombGame game=null;
    @Override
    protected void executeAux(CommandEvent event) {
        User user = Application.getUser(event);

        //  TEST FOR CODE NAME
//        event.getChannel().sendMessage(new DiscordCodeNameTable(new Random().nextBoolean()).getEmbedWithoutReveled()).queue();
//        event.getChannel().sendMessage(new DiscordCodeNameTable(new Random().nextBoolean()).getEmbedWithReveled()).queue();

        //  TEST FOR TIMES BOMB
        if(verif) {
            int NB_PLAYER = 4;
            Player[] players = new Player[NB_PLAYER];
            for (int i = 0;i<NB_PLAYER;i++) players[i]=new Player(user, i);
            game = new TimesBombGame(players,1,2);
            game.setOut(new TimesBombOut(event.getChannel()));
            game.start();
            verif=false;
        } else {
            game.kill();
            verif=true;
        }

        // TEST FOR MASCARADE
//        int NB_PLAYER = 13;
//        Player[] players = new Player[NB_PLAYER];
//        for (int i = 0;i<NB_PLAYER;i++) players[i]=new Player(user, i);
//        MascaradeFactory factory = new MascaradeFactory();
//        factory.setPlayers(players);
//        factory.setDiscordOut(event.getChannel());
//        MascaradeGame game = factory.createGame();
//        game.disableStartingTurn();
//        QuestionAgain.gameForTest = game;
//        game.start();

        // TEST FOR MY_FUTURE
//        new Thread(()->futureTest(event)).start();

    }

    private void futureTest(CommandEvent event){
        MyFuture<Integer> answer = new MyFuture<>();
        QuestionSender sender = new QuestionSender(new ArraysChoice<>(
                "Quelle action souhaitez-vous réaliser ?",
                new Integer[]{1,2,3,4},
                answer::sendAnswer));
        sender.disableEndMsg();
        sender.setTarget(event.getAuthor());
        sender.send(event.getChannel());
        event.getChannel().sendMessage("La réponse "+answer.get()+" est bien revenu").queue();
    }
}
