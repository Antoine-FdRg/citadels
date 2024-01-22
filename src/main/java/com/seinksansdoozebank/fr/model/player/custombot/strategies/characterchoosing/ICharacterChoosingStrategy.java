package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

/**
 * Represents a strategy to choose a character
 */
public interface ICharacterChoosingStrategy {
    /**
     * Apply the strategy to choose a character for a player
     *
     * @param player     the player who choose a character
     * @param characters the list of characters the player can choose
     * @return the chosen character
     */
    Character apply(Player player, List<Character> characters);
}
