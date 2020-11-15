package com.bdj.bot_discord.games.code_name;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class WordList {
    private final static String LIST_PATH = "rsrc/words/code_name_word.json";
    public final static WordList list = new WordList();

    private final Random random = new Random();
    private final ObjectMapper mapper = new ObjectMapper();

    private List<String> unUsed = new LinkedList<>();
    private List<String> used = new LinkedList<>();

    private WordList(){
        String[] tab = new String[0];

        try{
            tab = mapper.readValue(filePathToString(LIST_PATH), String[].class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        unUsed.addAll(Arrays.asList(tab));
    }

    public String getRandom(){
        if(unUsed.isEmpty()) transverse();
        String result = unUsed.remove(random.nextInt(unUsed.size()));
        used.add(result);
        return result;
    }

    public List<String> getRandoms(int nb){
        List<String> result = new LinkedList<>();
        for(int i=0;i<nb;i++) result.add(getRandom());
        return result;
    }

    private void transverse() {
        unUsed.addAll(used);
        used.clear();
    }

    private static String filePathToString(String filePath){
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
