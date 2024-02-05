package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;
import com.seinksansdoozebank.fr.view.IView;

import java.util.List;

public class RichardBot extends SmartBot {

    public RichardBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    protected Character chooseAssassinTarget() {
        List<Character> charactersList = this.getAvailableCharacters();
        // Conditions spécifiques pour Voleur et Condottiere
        Character target = null;
        for (Character character : charactersList) {
            if (character.getRole() == Role.THIEF && (shouldPreventWealth() || thinkThiefWillBeChosenByTheLeadingOpponent())
                    || character.getRole() == Role.CONDOTTIERE && (this.isAboutToWin() || thinkCondottiereWillBeChosenByTheLeadingOpponent())) {
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
     * Vérifie si le Condottiere sera choisi par l'adversaire en tête
     *
     * @return true si l'adversaire en tête est sur le point de gagner, false sinon
     */
    boolean thinkCondottiereWillBeChosenByTheLeadingOpponent() {
        // if leadingOpponent is about to win, he will choose Condottiere or Bishop, but the bishop is not killable
        return StrategyUtils.getLeadingOpponent(this).isAboutToWin();
    }

    boolean thinkThiefWillBeChosenByTheLeadingOpponent() {
        // if leadingOpponent is about to win, he will choose Thief
        return this.getOpponents().stream().anyMatch(Opponent::isAboutToWin);
    }

}
