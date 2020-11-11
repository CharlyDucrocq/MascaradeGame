package com.bdj.bot_discord.games.mascarade.card;

import com.bdj.bot_discord.games.mascarade.MascaradeGame;
import com.bdj.bot_discord.games.mascarade.Player;

public interface CardCreator {
    Card getCard(Player player, MascaradeGame game);
}
