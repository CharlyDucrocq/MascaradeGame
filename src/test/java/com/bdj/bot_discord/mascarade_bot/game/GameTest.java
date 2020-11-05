package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private int NB_PLAYERS = 7;

    Game game;
    User[] users;


    @BeforeEach
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

    @Test
    void trueSwitchCardTest(){
        GameRound round = game.getRound();
        Player p1 = round.player;
        Player p2 = game.getPlayer(users[users.length-1]);
        Character p1Char = p1.getCurrentCharacter();
        Character p2Char = p2.getCurrentCharacter();
        round.switchCard(p2, true);
        assertEquals(p1Char, p2.getCurrentCharacter());
        assertEquals(p2Char, p1.getCurrentCharacter());
    }

    @Test
    void fakeSwitchCardTest(){
        GameRound round = game.getRound();
        Player p1 = round.player;
        Player p2 = game.getPlayer(users[users.length-1]);
        Character p1Char = p1.getCurrentCharacter();
        Character p2Char = p2.getCurrentCharacter();
        round.switchCard(p2, false);
        assertEquals(p1Char, p1.getCurrentCharacter());
        assertEquals(p2Char, p2.getCurrentCharacter());
    }

    @Test
    void roundEndAfterSwitchCard(){
        GameRound round = game.getRound();
        Player p2 = game.getPlayer(users[users.length-1]);
        round.switchCard(p2, true);
        assertTrue(round.isEnded());
        assertNotEquals(round, game.getRound());
    }

    @Test
    void roundEndAfterFakeSwitchCard(){
        GameRound round = game.getRound();
        Player p2 = game.getPlayer(users[users.length-1]);
        round.switchCard(p2, false);
        assertTrue(round.isEnded());
        assertNotEquals(round, game.getRound());
    }

    @Test
    void peekTest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        round.peekCharacter();
        // TODO check
    }

    @Test
    void roundEndAfterPeek(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        round.peekCharacter();
        assertTrue(round.isEnded());
        assertNotEquals(round, game.getRound());
    }

    @Test
    void contestTest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        Player p2 = game.getPlayer(users[users.length-1]);
        int before = round.contestPlayers.size();
        assertEquals(0, before);
        round.contest(p2);
        assertEquals(before+1,round.contestPlayers.size());

        // now, let's check removing
        round.contest(p2);
        assertEquals(before,round.contestPlayers.size());
    }

    @Test
    void roundNotEndAfterContest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        Player p2 = game.getPlayer(users[users.length-1]);
        round.contest(p2);
        assertFalse(round.isEnded());
        assertEquals(round, game.getRound());
    }

    @Test
    void useCharacterTest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        /**  TODO
         * round.setCharacterToUse(///);
         * round.useCharacter();
         * tests...
         */
    }

    private void passPreliminaryRounds(){
        //TODO
    }
}
