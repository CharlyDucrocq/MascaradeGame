package com.bdj.bot_discord.mascarade_bot.game;

import com.bdj.bot_discord.discord.User;
import com.bdj.bot_discord.lobby.GameFactory;
import com.bdj.bot_discord.mascarade_bot.errors.GameException;
import com.bdj.bot_discord.mascarade_bot.game.card.Character;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.bdj.bot_discord.mascarade_bot.game.card.Character.*;

public class MascaradeFactory implements GameFactory<MascaradeGame> {
    private Player[] players;
    private Random random = new Random();

    private MascaradeOut out;

    public void setPlayers(Collection<User> users){
        Player[] tab = new Player[users.size()];
        int i = 0;
        for (User user : users) tab[i++] = new Player(user);
        this.players = tab;
    }

    @Override
    public boolean haveEnoughPlayer(){
        return players.length>=GlobalParameter.MIN_PLAYERS;
    }


    public void setDiscordOut(MessageChannel channel){
        this.out = new MascaradeOut(channel);
    }

    public MascaradeGame createGame(){
        List<Character> characters = new LinkedList<>();
        characters.add(JUDGE);
        switch (players.length){
            case 6 : {
                characters.add(BISHOP);
                characters.add(KING);
                characters.add(QUEEN);
                characters.add(WITCH);
                characters.add(CHEATER);
                break;
            }
            case 7 : {
                characters.add(BISHOP);
                characters.add(KING);
                characters.add(QUEEN);
                characters.add(WITCH);
                characters.add(BEGGAR);
                characters.add(THIEF);
                break;
            }
            case 8 : {
                characters.add(BISHOP);
                characters.add(KING);
                characters.add(QUEEN);
                characters.add(WITCH);
                characters.add(PEASANT);
                characters.add(PEASANT);
                characters.add(THIEF);
                break;
            }
            default: {
                throw new GameException("Mauvais nombre de joueurs");
            }
        }
        if(characters.size()==1) return null;
        for(Player p : players){
            p.setCurrentCharacter(characters.remove(random.nextInt(characters.size())));
        }

        MascaradeGame game = new MascaradeGame(players);
        game.setOut(out);
        return game;
    }
}
