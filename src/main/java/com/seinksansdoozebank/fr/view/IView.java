package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

/**
 * The view interface
 */
public interface IView {
    /**
     * Display the player's information
     *
     * @param player the player
     */
    void displayPlayerInfo(Player player);

    /**
     * Display the round number
     * @param roundNumber the round number
     */
    void displayRound(int roundNumber);

    /**
     * Display that the player plays a card
     * @param player the player who plays the card
     * @param card the card played
     */
    void displayPlayerPlaysCard(Player player, Card card);

    /**
     * Display the winner of the game
     * @param winner the winner
     */
    void displayWinner(Player winner);

    /**
     * Display that the player is beginning to play
     * @param player the player
     */
    void displayPlayerStartPlaying(Player player);

    /**
     * Display that the player picks cards
     * @param player the player
     * @param numberOfCards the number of cards picked
     */
    void displayPlayerPickCards(Player player, int numberOfCards);

    /**
     * Display that the player picks gold
     * @param player the player
     * @param numberOfGold the number of gold picked
     */
    void displayPlayerPicksGold(Player player, int numberOfGold);

    /**
     * Display that the player choose a character
     * @param player the player
     */
    void displayPlayerChooseCharacter(Player player);

    /**
     * Display that the player reveal his character
     * @param player the player
     */
    void displayPlayerRevealCharacter(Player player);

    /**
     * Display that the player use the warlord effect
     * @param attacker the attacker
     * @param defender the defender
     * @param district the district destroyed
     */
    void displayPlayerUseWarlordDistrict(Player attacker, Player defender, District district);

    /**
     * Display the player's score
     * @param player the player
     */
    void displayPlayerScore(Player player);

    /**
     * Display the player's bonus
     * @param player the player
     * @param pointsBonus the number of points
     * @param bonusName the name of the bonus
     */
    void displayPlayerGetBonus(Player player, int pointsBonus, String bonusName);

    /**
     * Display the game is finished
     */
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

    /**
     * Display the assassin effect on a character
     * @param player the player who use the assassin
     * @param target the target of the assassin
     */
    void displayPlayerUseAssassinEffect(Player player, Character target);

    /**
     * Display unused character in the round
     * @param character the character
     */
    void displayUnusedCharacterInRound(Character character);

    /**
     * Display the character who has been stolen
     * @param character the character
     */
    void displayStolenCharacter(Character character);

    /**
     * Display the actual number of gold of the player
     * @param player the player
     */
    void displayActualNumberOfGold(Player player);

    /**
     * Display the magician effect on a player
     * @param player the player who use the magician
     * @param targetPlayer the target of the magician
     */
    void displayPlayerUseMagicianEffect(Player player, Opponent targetPlayer);

    /**
     * Display the player has got the observatory and its effect
     * @param player the player
     */
    void displayPlayerHasGotObservatory(Player player);

    /**
     * Display the thief effect of a player
     * @param player the player who use the thief
     */
    void displayPlayerUseThiefEffect(Player player);

    /**
     * Display player discard a card
     * @param player the player
     * @param card the card discarded
     */
    void displayPlayerDiscardCard(Player player, Card card);

    /**
     * Display the player use the laboratory effect
     * @param player the player
     */
    void displayPlayerUseLaboratoryEffect(Player player);

    /**
     * Display the player use the manufacture effect
     * @param player the player
     */
    void displayPlayerUseManufactureEffect(Player player);

    /**
     * Display the gold collected from a district type
     * @param player the player
     * @param nbGold the number of gold
     * @param districtType the district type
     */
    void displayGoldCollectedFromDistrictType(Player player, int nbGold, DistrictType districtType);

    /**
     * Display the player use Library effect and keep both cards
     * @param player the player
     */
    void displayPlayerKeepBothCardsBecauseOfLibrary(Player player);

    /**
     * Display that the game is stuck
     */
    void displayGameStuck();

    /**
     * Display the player use the cemetery effect
     * @param player the player
     * @param card the card used
     */
    void displayPlayerUseCemeteryEffect(Player player, Card card);

    /**
     * Display the gold collected from the merchant by using the merchant effect
     * @param player the player
     */
    void displayGoldCollectedFromMerchant(Player player);
}
