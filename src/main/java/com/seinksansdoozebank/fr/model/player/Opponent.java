package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;

import java.util.List;

public interface Opponent {

    int getNbGold();

    int nbDistrictsInCitadel();

    List<Card> getCitadel();

    Character getOpponentCharacter();

    void switchHandWith(Player player);

    int getHandSize();

    boolean equals(Object o);

    boolean isAboutToWin();

    void havingADistrictDestroyed(Player attacker, District district);

    boolean isUsingCemeteryEffect(Card card);

}
