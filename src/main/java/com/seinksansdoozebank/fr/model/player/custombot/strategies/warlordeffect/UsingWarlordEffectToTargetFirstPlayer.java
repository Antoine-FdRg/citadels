package com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * This strategy targets the first player in the game and try to destroy his cheapest district
 */
public class UsingWarlordEffectToTargetFirstPlayer implements IUsingWarlordEffectStrategy {

    @Override
    public WarlordTarget apply(Player player, List<Opponent> opponents) {
        Opponent targetOpponent = StrategyUtils.getLeadingOpponent(opponents);
        if ((targetOpponent.getOpponentCharacter() != null && targetOpponent.getOpponentCharacter().getRole() == Role.BISHOP) || targetOpponent.nbDistrictsInCitadel() >= 8) {
            return null;
        }
        Optional<Card> cheaperCardToDestroy = targetOpponent.getCitadel().stream()
                .filter(card -> card.getDistrict().getCost() < player.getNbGold() && card.getDistrict() != District.DONJON)
                .min(Comparator.comparingInt(c -> c.getDistrict().getCost()));
        return cheaperCardToDestroy.map(card -> new WarlordTarget(targetOpponent, card.getDistrict())).orElse(null);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof UsingWarlordEffectToTargetFirstPlayer;
    }

    @Override
    public int hashCode() {
        return UsingWarlordEffectToTargetFirstPlayer.class.getName().hashCode();
    }
}
