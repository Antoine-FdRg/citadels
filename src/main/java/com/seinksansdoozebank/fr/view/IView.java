package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

public interface IView {
    void displayPlayerInfo(Player player);

    void displayRound(int roundNumber);

    void displayPlayerPlaysCard(Player player, Card card);

    void displayWinner(Player winner);

    void displayPlayerStartPlaying(Player player);

    void displayPlayerPickCards(Player player, int numberOfCards);

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
     * @param player   the player
     * @param strategy the message to display
     */
    void displayPlayerStrategy(Player player, String strategy);

    void displayPlayerUseAssassinEffect(Player player, Character target);

    void displayUnusedCharacterInRound(Character character);

    void displayStolenCharacter(Character character);

    void displayActualNumberOfGold(Player player);

    void displayPlayerUseMagicianEffect(Player player, Opponent targetPlayer);

    void displayPlayerHasGotObservatory(Player player);

    void displayPlayerUseThiefEffect(Player player);

    void displayPlayerDiscardCard(Player player, Card card);

    void displayPlayerUseLaboratoryEffect(Player player);

    void displayPlayerUseManufactureEffect(Player player);
}
