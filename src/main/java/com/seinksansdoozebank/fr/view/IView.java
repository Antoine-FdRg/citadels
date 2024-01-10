package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public interface IView {
    void displayPlayerInfo(Player player);

    void displayRound(int roundNumber);

    void displayPlayerPlaysCard(Player player, List<Card> optionalCard);

    void displayWinner(Player winner);

    void displayPlayerStartPlaying(Player player);

    void displayPlayerPickCard(Player player);

    void displayPlayerPicksGold(Player player);

    void displayPlayerChooseCharacter(Player player);

    void displayPlayerRevealCharacter(Player player);

    void displayPlayerDestroyDistrict(Player attacker, Player defender, District district);

    void displayPlayerScore(Player player);

    void displayPlayerGetBonus(Player player, int pointsBonus, String bonusName);

    void displayGameFinished();

    /**
     * Display the error message of a player action
     *
     * @param player  the player
     * @param message the message to display
     */
    void displayPlayerError(Player player, String message);

    /**
     * Display the strategy of the player
     * Here we use a function which take a message because the log level is fine and this will be the only function which log the strategy
     *
     * @param player  the player
     * @param strategy the message to display
     */
    void displayPlayerStrategy(Player player, String strategy);
}
