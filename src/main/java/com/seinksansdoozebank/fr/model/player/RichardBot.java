package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RichardBot extends SmartBot {

    public RichardBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    boolean anOpponentIsAboutToWin() {
        return this.getOpponents().stream().anyMatch(Opponent::isAboutToWin);
    }

    @Override
    protected Optional<Character> chooseThiefTarget() {
        List<Role> charactersInTheRound = this.getAvailableCharacters().stream().map(Character::getRole).toList();
        if (this.anOpponentIsAboutToWin()) {
            if (charactersInTheRound.contains(Role.BISHOP)) {
                return Optional.ofNullable(StrategyUtils.getCharacterFromRoleInLIst(Role.BISHOP, this.getAvailableCharacters()));
            } else if (charactersInTheRound.contains(Role.CONDOTTIERE)) {
                return Optional.ofNullable(StrategyUtils.getCharacterFromRoleInLIst(Role.CONDOTTIERE, this.getAvailableCharacters()));
            }
        }
        return useSuperChoseThiefEffect();
    }

    Optional<Character> useSuperChoseThiefEffect() {
        return super.chooseThiefTarget();
    }

    @Override
    protected Character chooseAssassinTarget() {
        List<Character> charactersList = this.getAvailableCharacters();
        // Conditions spécifiques pour Voleur et Condottiere
        Character target = null;
        for (Character character : charactersList) {
            if (character.getRole() == Role.THIEF && (shouldPreventWealth() || thinkThiefHasBeenChosenByTheLeadingOpponent())
                    || character.getRole() == Role.CONDOTTIERE && (this.isAboutToWin() || thinkCondottiereHasBeenChosenByTheLeadingOpponent())) {
                target = character;
            }
        }
        if (target == null) {
            return super.chooseAssassinTarget();
        }
        return target;
    }

    /**
     * Vérifie si un adversaire a un grand nombre de pièces d'or (7 ou plus)
     *
     * @return true si un adversaire a 7 pièces d'or ou plus, false sinon
     */
    boolean shouldPreventWealth() {
        return this.getOpponents().stream().anyMatch(opponent -> opponent.getNbGold() > 7);
    }

    /**
     * Check if the Condottiere will be chosen by the leading opponent
     *
     * @return true if the player think Condottiere has been chosen by the leading opponent, false otherwise
     */
    boolean thinkCondottiereHasBeenChosenByTheLeadingOpponent() {
        // if leadingOpponent is about to win, he will choose Condottiere or Bishop, but the bishop is not killable
        return StrategyUtils.getLeadingOpponent(this).isAboutToWin();
    }

    /**
     * Check if the Thief has been chosen by the leading opponent
     *
     * @return true if the player think Thief has been chosen by the opponent about to win, false otherwise
     */
    boolean thinkThiefHasBeenChosenByTheLeadingOpponent() {
        Thief thief = new Thief();
        if (this.getCharactersNotInRound().contains(thief)) { // if thief is not in the round, we return false (because he can't be chosen)
            return false;
        }
        if (this.getCharactersSeenInRound().contains(thief)) { // if thief has been seen (means that he has been chosen after the player)
            return this.getOpponentsWhichHasChosenCharacterAfter().stream().anyMatch(Opponent::isAboutToWin); // we check if the opponent which is about to win has chosen after the player (could mean that he has chosen the thief)
        } else {
            return this.getOpponentsWhichHasChosenCharacterBefore().stream().anyMatch(Opponent::isAboutToWin); // if the thief has not been seen, we check if the opponents which has chosen before the player are about to win (could mean that the thief has been chosen by one of them)
        }
    }

    /**
     * Get the opponents which has chosen their character before the player
     * make difference between this.getOpponents() and this.getOpponentsWhichHasChosenCharacterBefore()
     *
     * @return the opponents which has chosen their character before the player
     */
    List<Opponent> getOpponentsWhichHasChosenCharacterAfter() {
        return this.getOpponents().stream().filter(opponent -> !this.getOpponentsWhichHasChosenCharacterBefore().contains(opponent)).toList();
    }

    /**
     * Use the magician effect to switch hand with the opponent which has the most districts
     *
     * @param magician the magician character
     */
    @Override
    protected void useEffectMagician(Magician magician) {
        Optional<Opponent> playerWithMostDistricts = this.getOpponents().stream()
                .max(Comparator.comparingInt(Opponent::getHandSize));
        playerWithMostDistricts.ifPresent(opponent -> magician.useEffect(opponent, null));
    }


}
