package com.bdj.bot_discord.games.code_name;

import com.bdj.bot_discord.discord.utils.User;
import com.bdj.bot_discord.lobby.Game;

import java.util.List;
import java.util.Random;

public class CodeNameGame implements Game {
    private final static Random random = new Random();
    private CodeNameOut out;

    private boolean over = false;

    private final Team blues;
    private final Team reds;
    private boolean blueOrRedTurn;

    CodeNameGame(List<Player> blueTeam, List<Player> redTeam){
        blues = new Team(blueTeam);
        reds = new Team(redTeam);
        blueOrRedTurn = random.nextBoolean();
    }

    @Override
    public void start() {
        //out.askForLeader(this);
    }

    void nextTurn(){
        blueOrRedTurn=!blueOrRedTurn;
        Team current = getCurrentTeam();
    }

    private Team getCurrentTeam() {
        return blueOrRedTurn ? blues : reds;
    }

    @Override
    public User[] getUsers() {
        User[] users = new User[blues.size()+reds.size()];
        int i=0;
        for (Player red : reds.getAll()) users[i++] = red.getUser();
        for (Player blue : blues.getAll()) users[i++] = blue.getUser();
        return users;
    }

    @Override
    public boolean isOver() {
        return over;
    }
}
