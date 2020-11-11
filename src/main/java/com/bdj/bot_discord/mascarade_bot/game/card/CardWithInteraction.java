package com.bdj.bot_discord.mascarade_bot.game.card;

import com.bdj.bot_discord.discord.QuestionSender;
import com.bdj.bot_discord.mascarade_bot.game.MascaradeOut;
import com.bdj.bot_discord.mascarade_bot.game.Player;
import com.bdj.bot_discord.mascarade_bot.utils.choice.ArraysChoice;
import com.bdj.bot_discord.mascarade_bot.utils.choice.QuestionAnswers;
import net.dv8tion.jda.api.entities.User;

public abstract class CardWithInteraction extends Card {
    protected MyLock lock = new MyLock();
    private QuestionAnswers questionAnswers;
    protected MascaradeOut out;

    public CardWithInteraction(Character type, Player player, MascaradeOut out) {
        super(type, player);
        this.out = out;
    }

    @Override
    public final void action() {
        lock.lock();
        QuestionAnswers question = actionBetweenLock();
        if (question != null)
            player.ask(question);
        lock.lock();
        lock.unlock();
    }

    protected abstract QuestionAnswers actionBetweenLock();

    protected static class MyLock{
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
