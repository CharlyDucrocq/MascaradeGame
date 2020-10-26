package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private int NB_PLAYERS = 7;

    Game game;
    User[] users;


    @BeforeAll
    void init(){
        Lobby lobby = new Lobby();
        users = new User[NB_PLAYERS];
        for(int i=0;i<NB_PLAYERS;i++){
            users[i] = mock(User.class);
            if(i==0) lobby.addAdmin(users[i]);
            else lobby.addPlayer(users[i]);
        }
        game = lobby.createGame();
        game.setInOut(mock(InOutGameInterface.class));
        game.start();
    }

    @Test
    void turnRoundWellExecuted(){
        User firstToPlay = game.getRound().getUser();
        for (int i= 0;i<NB_PLAYERS-1;i++) {
            game.nextRound();
            assertNotEquals(firstToPlay, game.getRound().getUser());
        }
        game.nextRound();
        assertEquals(firstToPlay, game.getRound().getUser());
    }

    private void passPreliminaryRounds(){
        //TODO
    }
}
