package com.bdj.bot_discord.utils;

import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyFuture<T> implements Future<T> {
    T answer = null;


    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        //  no utility here
        return false;
    }

    @Override
    public boolean isCancelled() {
        // no utility here
        return false;
    }

    @Override
    public boolean isDone() {
        return answer != null;
    }

    @Override
    public synchronized T get() {
        try{
            while(!isDone()) wait();
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return answer;
    }

    @Override
    public synchronized T get(long timeout, @NotNull TimeUnit unit) {
        Instant start = Instant.now();
        long timeLimit = unit.toMillis(timeout);
        long stillToDo = timeLimit;
        try {
            while(!isDone()) {
                wait(stillToDo);
                stillToDo = timeLimit - Duration.between(start, Instant.now()).toMillis();
                if(stillToDo<=0) throw new TimeoutException();
            }
        } catch (RuntimeException e){
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return answer;
    }

    public synchronized void sendAnswer(T answer) {
        if(isDone())
            throw new RuntimeException("Already done");
        this.answer = answer;
        System.out.println(answer);
        this.notifyAll();
    }
}
