package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.mascarade_bot.discord.User;
import com.bdj.bot_discord.mascarade_bot.game.card.Card;
import com.bdj.bot_discord.mascarade_bot.game.card.CardCreator;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import com.bdj.bot_discord.mascarade_bot.utils.InOutGameInterface;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private int NB_PLAYERS = 7;

    Game game;
    User[] users;
    Player[] players;
    InOutGameInterface inOutMock;


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
        inOutMock = mock(InOutGameInterface.class);
        game.setInOut(inOutMock);
        game.start();
        players = game.getTable().getPlayers();
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
        User user = round.getUser();
        clearInvocations(inOutMock);
        round.peekCharacter();
        verify(inOutMock,atLeast(1)).printPersonalMsg(eq(user),anyString());
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
    void timeBeforeUseTest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        round.setCharacterToUse(Character.JUDGE);
        Instant instant =round.characterChoiceInstant;
        assertThrows(RuntimeException.class, round::useCharacter);
        round.characterChoiceInstant=instant.minus(GlobalParameter.CHOICE_USE_TIME_IN_SEC/2, ChronoUnit.SECONDS);
        assertThrows(RuntimeException.class, round::useCharacter);
        round.characterChoiceInstant=instant.minus(GlobalParameter.CHOICE_USE_TIME_IN_SEC, ChronoUnit.SECONDS);
        assertDoesNotThrow(round::useCharacter);
    }

    @Test
    void characterSelectionTest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        assertThrows(RuntimeException.class,()->round.setCharacterToUse(Character.NEVER_ON_GAME));
        assertDoesNotThrow(()->round.setCharacterToUse(Character.JUDGE));
        assertEquals(Character.JUDGE, round.charaChose);
    }

    @Test
    void useCharacterWhenTrueAndNoContest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        CardCreator mockChar = mock(CardCreator.class);
        Card mockCard = mock(Card.class);
        when(mockChar.getCard(any(),any())).thenReturn(mockCard);
        when(mockCard.getType()).thenReturn(round.player.getCurrentCharacter());
        round.setCharacterToUse(round.player.getCurrentCharacter());
        passRoundTime(round);
        round.useCharacterOnlyForTest(mockChar);
        verify(mockChar,times(1)).getCard(eq(round.player), any());
        verify(mockCard,times(1)).action();
    }

    @Test
    void useCharacterWhenFakeAndNoContest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        CardCreator mockChar = mock(CardCreator.class);
        Card mockCard = mock(Card.class);
        when(mockChar.getCard(any(),any())).thenReturn(mockCard);
        when(mockCard.getType()).thenReturn(game.getTable().getPrev().getCurrentCharacter());
        round.setCharacterToUse(game.getTable().getPrev().getCurrentCharacter());
        passRoundTime(round);
        round.useCharacterOnlyForTest(mockChar);

        verify(mockChar,times(1)).getCard(eq(round.player), any());
        verify(mockCard,times(1)).action();
    }

    @Test
    void useCharacterWhenFalseAndSomeContest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        Player player = round.player;
        Player trueRole = game.getTable().getPrev();

        int HOW_MANY_CONTEST = 3;
        List<Player> contestators = new LinkedList<>();
        for(int i=0;contestators.size()<HOW_MANY_CONTEST-1;i++){
            if(players[i]!=player && players[i]!=trueRole) contestators.add(players[i]);
        }
        contestators.add(trueRole);

        game.setBankOnlyForTest(mock(Bank.class));
        CardCreator mockChar = mock(CardCreator.class);
        Card mockCard = mock(Card.class);
        when(mockChar.getCard(any(),any())).thenReturn(mockCard);
        when(mockCard.getType()).thenReturn(trueRole.getCurrentCharacter());

        round.setCharacterToUse(trueRole.getCurrentCharacter());
        for (Player contestator : contestators) round.contest(contestator);
        passRoundTime(round);
        round.useCharacterOnlyForTest(mockChar);

        verify(mockChar,times(1)).getCard(eq(trueRole), any());
        verify(mockCard,times(1)).action();

        verify(game.getBank(), times(1)).takeTaxFrom(eq(player));
        for (Player contestator : contestators)
            if(contestator!=trueRole)
                verify(game.getBank(), times(1)).takeTaxFrom(eq(contestator));
    }

    @Test
    void useCharacterWhenTrueAndSomeContest(){
        passPreliminaryRounds();
        GameRound round = game.getRound();
        Player player = round.player;

        int HOW_MANY_CONTEST = 3;
        List<Player> contestators = new LinkedList<>();
        for(int i=0;contestators.size()<HOW_MANY_CONTEST;i++){
            if(players[i]!=player) contestators.add(players[i]);
        }

        game.setBankOnlyForTest(mock(Bank.class));
        CardCreator mockChar = mock(CardCreator.class);
        Card mockCard = mock(Card.class);
        when(mockChar.getCard(any(),any())).thenReturn(mockCard);
        when(mockCard.getType()).thenReturn(player.getCurrentCharacter());

        round.setCharacterToUse(player.getCurrentCharacter());
        for (Player contestator : contestators) round.contest(contestator);
        passRoundTime(round);
        round.useCharacterOnlyForTest(mockChar);

        verify(mockChar,times(1)).getCard(eq(player), any());
        verify(mockCard,times(1)).action();

        for (Player contestator : contestators)
            verify(game.getBank(), times(1)).takeTaxFrom(eq(contestator));
    }

    private static void passRoundTime(GameRound round) {
        round.characterChoiceInstant=round.characterChoiceInstant.minus(GlobalParameter.CHOICE_USE_TIME_IN_SEC, ChronoUnit.SECONDS);
    }

    private void passPreliminaryRounds(){
        while (game.isInPreliminary())
            game.nextRound();
        game.nextRound();
    }
}
