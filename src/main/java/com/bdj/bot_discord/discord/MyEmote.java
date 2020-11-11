package com.bdj.bot_discord.discord;

import com.vdurmont.emoji.EmojiParser;

public enum MyEmote {
    ONE("one"),
    TWO("two"),
    THREE("three"),
    FOUR("four"),
    FIVE("five"),
    SIX("six"),
    SEVEN("seven"),
    EIGHT("eight"),
    NINE("nine"),
    ZERO("zero"),

    YES("white_check_mark"),
    NOP("negative_squared_cross_mark"),
    OBJECTION("speaking_head"),
    ;

    public final String name;

    MyEmote(String name){
        this.name = ":"+name+":";
    }

    public String getId(){
        return EmojiParser.parseToUnicode(name);
    }

    public static MyEmote getFromId(String id){
        for (MyEmote emote : values()) if (emote.getId().equals(id)) return emote;
        return null;
    }
}
