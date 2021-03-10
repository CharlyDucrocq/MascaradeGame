package com.bdj.bot_discord.discord.utils;

import java.util.function.Consumer;

public class AntiErrorConsumer<T> implements Consumer<T> {
    private final Consumer<T> consumer;
    public AntiErrorConsumer(Consumer<T> consumer){
        this.consumer =consumer;
    }

    @Override
    public void accept(T t) {
        try {
            consumer.accept(t);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
