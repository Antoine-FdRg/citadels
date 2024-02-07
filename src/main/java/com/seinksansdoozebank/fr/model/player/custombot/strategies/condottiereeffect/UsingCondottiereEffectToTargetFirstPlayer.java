package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.CondottiereTarget;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;

import java.util.Comparator;
import java.util.Optional;

/**
 * This strategy targets the first player in the game and try to destroy his cheapest district
 */
public class UsingCondottiereEffectToTargetFirstPlayer implements IUsingCondottiereEffectStrategy {

    @Override
    public CondottiereTarget apply(Player player) {
        Opponent targetOpponent = StrategyUtils.getLeadingOpponent(player);
        if ((targetOpponent.getOpponentCharacter() != null && targetOpponent.getOpponentCharacter().getRole() == Role.BISHOP) || targetOpponent.nbDistrictsInCitadel() >= 8) {
            return null;
        }
        Optional<Card> cheaperCardToDestroy = targetOpponent.getCitadel().stream()
                .filter(card -> card.getDistrict().getCost() < player.getNbGold() && card.getDistrict() != District.DONJON)
                .min(Comparator.comparingInt(c -> c.getDistrict().getCost()));
        return cheaperCardToDestroy.map(card -> new CondottiereTarget(targetOpponent, card.getDistrict())).orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UsingCondottiereEffectToTargetFirstPlayer;
    }

    @Override
    public int hashCode() {
        return UsingCondottiereEffectToTargetFirstPlayer.class.getName().hashCode();
    }
}
