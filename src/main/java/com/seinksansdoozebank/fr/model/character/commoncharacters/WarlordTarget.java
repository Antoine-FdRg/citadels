package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Opponent;

/**
 * The warlord target
 *
 * @param opponent the opponent to destroy the district
 * @param district the district to destroy
 */
public record WarlordTarget(Opponent opponent, District district) {
}
