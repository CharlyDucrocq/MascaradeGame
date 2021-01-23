package com.bdj.bot_discord.discord.utils;

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
    OBJECTION("thumbsdown"),

    RED("red_square"),
    BLUE("blue_square"),
    POOP("poop"),
    DEATH("skull"),
    QUESTION("grey_question"),

    DICE("game_die"),
    OK("ok"),
    NEW("new"),
    ASTERISK("asterisk"),
    EXCLAMATION("exclamation")
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
