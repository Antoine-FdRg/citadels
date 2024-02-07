package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;

import java.util.List;
import java.util.Random;

/**
 * Represents a strategy to choose a character by taking the one that will be the
 * most useful to slow down the player who is the closest to win
 */
public class ChoosingCharacterToTargetFirstPlayer implements ICharacterChoosingStrategy {

    private static final int NB_GOLD_MIN_FOR_MANY_GOLD = 3;
    private final Random random;

    /**
     * Creates a new instance of ChoosingCharacterToTargetFirstPlayer
     */
    public ChoosingCharacterToTargetFirstPlayer() {
        this.random = new Random();
    }

    @Override
    public Character apply(Player player, List<Character> characters) {
        if (StrategyUtils.isRoleInCharacterList(Role.CONDOTTIERE, characters)) {                // si le condottiere est disponible
            return StrategyUtils.getCharacterFromRoleInLIst(Role.CONDOTTIERE, characters);      // on le prend pour détruire un quartier du leadingOpponent
        }
        Opponent leadingOpponent = StrategyUtils.getLeadingOpponent(player.getOpponents());                    // on récupère le leadingOpponent pour regarder som nombre de pièces
        if (leadingOpponent.getNbGold() > NB_GOLD_MIN_FOR_MANY_GOLD) {                          // s'il a beaucoup de pièces
            if (StrategyUtils.isRoleInCharacterList(Role.THIEF, characters)) {                  // et si le voleur est disponible
                return StrategyUtils.getCharacterFromRoleInLIst(Role.THIEF, characters);        // on le prend pour le voler (car en ayant beaucoup de pièces, il va probablement construire)
            }
            if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters)) {              // sinon, si l'architecte est disponible
                return StrategyUtils.getCharacterFromRoleInLIst(Role.ARCHITECT, characters);    // on le prend pour l'empêcher de construire avec toutes ses pièces
            }
        } else {                                                                                // sinon
            if (StrategyUtils.isRoleInCharacterList(Role.ASSASSIN, characters)) {               // si l'assassin est disponible
                return StrategyUtils.getCharacterFromRoleInLIst(Role.ASSASSIN, characters);     // on le prend pour tuer le marchand (car en ayant peu de pièces, il va probablement essayer d'en gagner)
            }
            if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters)) {               // si le marchand est disponible
                return StrategyUtils.getCharacterFromRoleInLIst(Role.MERCHANT, characters);     // on le prend pour empêcher le leadingOpponent de gagner des pièces
            }
        }
        if (StrategyUtils.isRoleInCharacterList(Role.KING, characters)) {                       // si rien de tout ça n'est disponible, on essaye de prendre le roi pour
            return StrategyUtils.getCharacterFromRoleInLIst(Role.KING, characters);             // pouvoir choisir en premier au prochain tour
        }
        return characters.get(this.random.nextInt(characters.size()));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof ChoosingCharacterToTargetFirstPlayer;
    }

    @Override
    public int hashCode() {
        return ChoosingCharacterToTargetFirstPlayer.class.getName().hashCode();
    }
}
