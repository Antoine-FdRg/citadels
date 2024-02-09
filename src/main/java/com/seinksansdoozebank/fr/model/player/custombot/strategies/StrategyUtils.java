package com.seinksansdoozebank.fr.model.player.custombot.strategies;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * The strategy utils
 */
public class StrategyUtils {
    /**
     * The random object
     */
    public static final Random RANDOM = new Random();

    private StrategyUtils() {
    }

    /**
     * Check if a role is in the character list
     *
     * @param role       the role to check
     * @param characters the list of characters
     * @return true if the role is in the list
     */
    public static boolean isRoleInCharacterList(Role role, List<Character> characters) {
        return characters.stream().anyMatch(character -> character.getRole().equals(role));
    }

    /**
     * Get the character from the role in the list
     *
     * @param role       the role to get
     * @param characters the list of characters
     * @return the character from the role in the list
     */
    public static Character getCharacterFromRoleInList(Role role, List<Character> characters) {
        return characters.stream().filter(character -> character.getRole().equals(role)).findFirst().orElse(null);
    }

    /**
     * Get a random character from the list
     *
     * @param characters the list of characters
     * @return a random character from the list
     */
    public static Character getRandomCharacterFromList(List<Character> characters) {
        return characters.get(RANDOM.nextInt(characters.size()));
    }

    /**
     * Get the leading opponent
     *
     * @param opponents the list of opponents
     * @return the leading opponent
     */
    public static Opponent getLeadingOpponent(List<Opponent> opponents) {
        Optional<Opponent> optionalOpponent = opponents.stream()
                .max(Comparator.comparingInt(Opponent::nbDistrictsInCitadel));
        if (optionalOpponent.isPresent()) {
            return optionalOpponent.get();
        } else {
            throw new IllegalStateException("No leading opponent found");
        }
    }
}
