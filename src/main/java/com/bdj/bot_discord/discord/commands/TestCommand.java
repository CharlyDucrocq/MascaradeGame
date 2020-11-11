package com.bdj.bot_discord.discord.commands;

import com.bdj.bot_discord.discord.QuestionSender;
import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeOut;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.choice.Answer;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.jagrosh.jdautilities.command.CommandEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static com.bdj.bot_discord.mascarade_bot.main.Application.*;

public class TestCommand extends ErrorCatcherCommand {
    private MyLock lock = new MyLock();

    public TestCommand(){
        this.name = "test";
        this.ownerCommand = true;
        this.guildOnly = false;
    }

    @Override
    protected void executeAux(CommandEvent event) {
        Player player = new Player(new User(event.getAuthor()));
        player.setCurrentCharacter(Character.WITCH);
        MascaradeOut out = new MascaradeOut(event.getChannel());
        out.printSwitch(player, player, true);
    }

    private static class MyLock{
        private boolean locked = false;
        public synchronized void lock(){
            try {
                if(locked) wait();
            } catch (Exception e){
                e.printStackTrace();
            }
            locked = true;
        }

        public synchronized void unlock(){
            locked = false;
            notifyAll();
        }
    }
}
