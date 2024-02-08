package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;

import java.util.List;

/**
 * Represents a strategy to choose a character
 */
public interface ICharacterChoosingStrategy {
    /**
     * Apply the strategy to choose a character for a player
     *
     * @param customBot     the player who choose a character
     * @param characters the list of characters the player can choose
     * @return the chosen character
     */
    Character apply(CustomBot customBot, List<Character> characters);
}
