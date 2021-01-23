package com.bdj.bot_discord.discord.commands;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum MyRole {
    GAME_MASTER;

    @Override
    public String toString() {
        //normalize : EXAMPLE_EXAMPLE => ExampleExample
        return Arrays
                .stream(super.toString().split("_"))
                .map(s->s.substring(0, 1).toUpperCase()+s.substring(1).toLowerCase())
                .collect(Collectors.joining());
    }
}
