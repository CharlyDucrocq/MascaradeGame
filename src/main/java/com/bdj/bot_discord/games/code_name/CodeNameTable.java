package com.bdj.bot_discord.games.code_name;

import com.bdj.bot_discord.discord.MyEmote;
import com.bdj.bot_discord.errors.GameException;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.bdj.bot_discord.games.code_name.WordList.*;

public class CodeNameTable {
    public final static Random random = new Random();
    public final static int WORD_BY_TABLE = 24;
    public final static int WORD_BY_TEAM = 8;
    protected final LinkedList<Word> table = new LinkedList<>();

    protected Word lastReveal;

    protected int nbRedLeft = WORD_BY_TEAM;
    protected int nbBlueLeft = WORD_BY_TEAM;
    protected final Type whoStart;

    public CodeNameTable(boolean blueFirst){
        whoStart = blueFirst ? Type.BLUE : Type.RED;
        int z = blueFirst ? nbBlueLeft++ : nbRedLeft++;
        List<Word> words = new LinkedList<>();
        for (int i = 0; i< nbRedLeft; i++) words.add(new Word(list.getRandom(), Type.RED));
        for (int i = 0; i< nbBlueLeft; i++) words.add(new Word(list.getRandom(), Type.BLUE));
        words.add(new Word(list.getRandom(), Type.BLACK));
        while (words.size()<WORD_BY_TABLE) words.add(new Word(list.getRandom(), Type.DEFAULT));


        for (int i=WORD_BY_TABLE;i>0;i--)
            table.add(words.remove(random.nextInt(i)));
    }

    public boolean reveal(String word){
        for (Word w : table) if (w.word.toLowerCase().equals(word.toLowerCase())) return reveal(w);
        throw new GameException("Bad word input");
    }

    protected boolean reveal(Word word){
        if(word.reveled) return false;
        lastReveal = word;
        word.reveled = true;
        if (word.type == Type.RED ) nbRedLeft--;
        if (word.type == Type.BLUE) nbBlueLeft--;
        return true;
    }

    protected static class Word{
        final String word;
        final Type type;
        boolean reveled = false;

        Word(String word, Type type){
            this.word = word;
            this.type = type;
            reveled = random.nextBoolean();
        }

        public String toStringWithEmote(){
            return reveled ? type.getEmoteId()+"\t~~"+word+"~~":type.getEmoteId()+"\t**"+word+"**";
        }

        @Override
        public String toString() {
            return reveled ? type.getEmoteId()+"\t~~"+word+"~~" : MyEmote.QUESTION.getId()+"\t**"+word+"**";
        }
    }

    protected static enum Type{
        RED(MyEmote.RED, Color.red),
        BLUE(MyEmote.BLUE, Color.blue),
        DEFAULT(MyEmote.POOP),
        BLACK(MyEmote.DEATH);

        private final MyEmote emote;
        private Color color;

        Type(MyEmote emote, Color color){
            this(emote);
            this.color = color;
        }

        Type(MyEmote emote){
            this.emote = emote;
        }

        String getEmoteId(){
            return emote.getId();
        }

        public Color getColor() {
            return color;
        }
    }
}
