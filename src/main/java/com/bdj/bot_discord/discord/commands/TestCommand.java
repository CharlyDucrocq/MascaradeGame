package com.bdj.bot_discord.discord.commands;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.games.mascarade.MascaradeOut;
import com.bdj.bot_discord.games.mascarade.Player;
import com.bdj.bot_discord.games.mascarade.card.Character;
import com.jagrosh.jdautilities.command.CommandEvent;

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
