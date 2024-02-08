package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
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
    public Character apply(CustomBot customBot, List<Character> characters) {
        Character character = null;
        Opponent leadingOpponent = StrategyUtils.getLeadingOpponent(customBot.getOpponents());                    // on récupère le leadingOpponent pour regarder son nombre de pièces
        Role roleToAvoid = null;
        if (customBot.getNbCharacterChosenInARow() >= Player.NB_MAX_CHARACTER_CHOSEN_IN_A_ROW) {
            roleToAvoid = customBot.getLastCharacterChosen().getRole();
        }
        if (StrategyUtils.isRoleInCharacterList(Role.CONDOTTIERE, characters) && Role.CONDOTTIERE != roleToAvoid) {                // si le condottiere est disponible
            character = StrategyUtils.getCharacterFromRoleInLIst(Role.CONDOTTIERE, characters);      // on le prend pour détruire un quartier du leadingOpponent
        } else if (leadingOpponent.getNbGold() > NB_GOLD_MIN_FOR_MANY_GOLD) {                          // s'il a beaucoup de pièces
            if (StrategyUtils.isRoleInCharacterList(Role.THIEF, characters) && Role.THIEF != roleToAvoid) {                  // et si le voleur est disponible
                character = StrategyUtils.getCharacterFromRoleInLIst(Role.THIEF, characters);        // on le prend pour le voler (car en ayant beaucoup de pièces, il va probablement construire)
            } else if (StrategyUtils.isRoleInCharacterList(Role.ARCHITECT, characters) && Role.ARCHITECT != roleToAvoid) {              // sinon, si l'architecte est disponible
                character = StrategyUtils.getCharacterFromRoleInLIst(Role.ARCHITECT, characters);    // on le prend pour l'empêcher de construire avec toutes ses pièces
            }
        } else {                                                                                // sinon
            if (StrategyUtils.isRoleInCharacterList(Role.ASSASSIN, characters) && Role.ASSASSIN != roleToAvoid) {               // si l'assassin est disponible
                character = StrategyUtils.getCharacterFromRoleInLIst(Role.ASSASSIN, characters);     // on le prend pour tuer le marchand (car en ayant peu de pièces, il va probablement essayer d'en gagner)
            } else if (StrategyUtils.isRoleInCharacterList(Role.MERCHANT, characters) && Role.MERCHANT != roleToAvoid) {               // si le marchand est disponible
                character = StrategyUtils.getCharacterFromRoleInLIst(Role.MERCHANT, characters);     // on le prend pour empêcher le leadingOpponent de gagner des pièces
            }
        }
        if (StrategyUtils.isRoleInCharacterList(Role.KING, characters) && Role.KING != roleToAvoid && character == null) {                       // si rien de tout ça n'est disponible, on essaye de prendre le roi pour
            character = StrategyUtils.getCharacterFromRoleInLIst(Role.KING, characters);             // pouvoir choisir en premier au prochain tour
        } else if (character == null) {
            character = characters.get(this.random.nextInt(characters.size()));
        }
        if (character.equals(customBot.getLastCharacterChosen())) {
            customBot.setNbCharacterChosenInARow(customBot.getNbCharacterChosenInARow() + 1);
        } else {
            customBot.setNbCharacterChosenInARow(1);
        }
        return character;
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
