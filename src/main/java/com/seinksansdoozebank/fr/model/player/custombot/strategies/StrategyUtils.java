package com.seinksansdoozebank.fr.model.player.custombot.strategies;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class StrategyUtils {

    private StrategyUtils() {
    }

    public static boolean isRoleInCharacterList(Role role, List<Character> characters) {
        return characters.stream().anyMatch(character -> character.getRole().equals(role));
    }

    public static Character getCharacterFromRoleInLIst(Role role, List<Character> characters) {
        return characters.stream().filter(character -> character.getRole().equals(role)).findFirst().orElseThrow();
    }

    public static Opponent getLeadingOpponent(Player player) {
        Optional<Opponent> optionalOpponent = player.getOpponents().stream()
                .filter(o -> !o.equals(player))
                .max(Comparator.comparingInt(Opponent::nbDistrictsInCitadel));
        if (optionalOpponent.isPresent()) {
            return optionalOpponent.get();
        } else {
            throw new IllegalStateException("No leading opponent found");
        }
    }
}
