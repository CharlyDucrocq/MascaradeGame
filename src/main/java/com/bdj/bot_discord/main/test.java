package com.bdj.bot_discord.main;

import com.bdj.bot_discord.discord.commands.MyRole;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        Integer test = new Integer(1);
        Map<Integer, Integer> map = new HashMap<>(){{
            put(1,test);
            put(2,test);
        }};
        System.out.println(new HashSet<>(map.values()));
    }
}
