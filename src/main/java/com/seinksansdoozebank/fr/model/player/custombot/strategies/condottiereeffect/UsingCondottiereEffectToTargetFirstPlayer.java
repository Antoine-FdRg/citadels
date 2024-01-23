package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Comparator;
import java.util.Optional;

/**
 *
 */
public class UsingCondottiereEffectToTargetFirstPlayer implements IUsingCondottiereEffectStrategy {

    @Override
    public void apply(Player player, Condottiere condottiere) {
        Opponent targetOpponent = getLeadingOpponent(player);
        Optional<Card> cardToDestroy = targetOpponent.getCitadel().stream().min(Comparator.comparingInt(c -> c.getDistrict().getCost()));
//        TODO uncomment this line when the method is fixed and ad tests
//        cardToDestroy.ifPresent(card -> condottiere.useEffect(targetOpponent, card.getDistrict()));
    }

    Opponent getLeadingOpponent(Player player) {
        Optional<Opponent> optionalOpponent = player.getOpponents().stream().filter(o -> !o.equals(player)).min((o1, o2) -> o2.nbDistrictsInCitadel() - o1.nbDistrictsInCitadel());
        if (optionalOpponent.isPresent()) {
            return optionalOpponent.get();
        } else {
            throw new IllegalStateException("No leading opponent found");
        }
    }
}
