package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;

import java.util.List;
import java.util.Optional;

public interface Opponent {

    int getNbGold();

    int nbDistrictsInCitadel();

    List<Card> getCitadel();

    Character getOpponentCharacter();

    void switchHandWith(Player player);

    int getHandSize();

    boolean equals(Object o);

    boolean isAboutToWin();

    Optional<Card> destroyDistrict(Player attacker, District district);

}
