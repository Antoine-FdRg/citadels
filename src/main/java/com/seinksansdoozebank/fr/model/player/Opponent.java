package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;

import java.util.List;

/**
 * Represent an opponent
 */
public interface Opponent {
    /**
     * Get the number of gold of the opponent
     *
     * @return the number of gold
     */
    int getNbGold();

    /**
     * Get the position in the draw to pick a character
     *
     * @return the position in the draw to pick a character
     */
    int getPositionInDrawToPickACharacter();

    /**
     * Get the number of districts in the citadel
     * @return the number of districts in the citadel
     */
    int nbDistrictsInCitadel();

    /**
     * Get the  districts in the citadel
     * @return the districts in the citadel
     */
    List<Card> getCitadel();

    /**
     * Get the character of the opponent only if the character is visible
     * @return the character of the opponent
     */
    Character getOpponentCharacter();

    /**
     * Switch the hand of the player with the hand of the target player (Magician effect)
     * @param player the player to switch the hand with
     */
    void switchHandWith(Player player);

    /**
     * Get the hand size of the opponent
     * @return the hand size of the opponent
     */
    int getHandSize();

    /**
     * Equals method
     * @param o the object to compare
     * @return true if the object is equal to this
     */
    boolean equals(Object o);

    /**
     * Check if the opponent is about to win
     * @return true if the opponent is about to win
     */
    boolean isAboutToWin();

    /**
     * Destroy a district of the opponent (Warlord effect)
     * @param attacker the attacker
     * @param district the district to destroy
     */
    void destroyDistrict(Player attacker, District district);

    /**
     * Use the cemetery effect
     * @param card the card to use the effect on
     * @return true if the effect has been used
     */
    boolean isUsingCemeteryEffect(Card card);

}
