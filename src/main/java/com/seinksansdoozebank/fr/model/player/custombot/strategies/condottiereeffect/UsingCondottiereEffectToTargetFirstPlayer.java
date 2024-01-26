package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
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
    public void apply(Player player, Condottiere condottiere) {
        Opponent targetOpponent = StrategyUtils.getLeadingOpponent(player);
        if ((targetOpponent.getOpponentCharacter() != null && targetOpponent.getOpponentCharacter().getRole() == Role.BISHOP) || targetOpponent.nbDistrictsInCitadel() >= 8) {
            return;
        }
        Optional<Card> cheaperCardToDestroy = targetOpponent.getCitadel().stream()
                .filter(card -> card.getDistrict().getCost() < player.getNbGold() && card.getDistrict() != District.DONJON)
                .min(Comparator.comparingInt(c -> c.getDistrict().getCost()));
        cheaperCardToDestroy.ifPresent(card -> condottiere.useEffect(targetOpponent, card.getDistrict()));
    }
}
