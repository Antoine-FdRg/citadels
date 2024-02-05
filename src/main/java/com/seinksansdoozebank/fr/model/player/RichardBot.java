package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;
import com.seinksansdoozebank.fr.view.IView;

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
}
