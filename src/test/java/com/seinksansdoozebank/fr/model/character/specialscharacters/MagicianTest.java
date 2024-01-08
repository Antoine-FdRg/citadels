package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MagicianTest {

    Player spyPlayer;
    Player otherSpyPlayer;
    Magician magician;
    Deck deck;
    Cli view;
    Card firstPickedCard;
    Card secondPickedCard;

    @BeforeEach
    void setUp() {
        view = mock(Cli.class);
        deck = mock(Deck.class);
        firstPickedCard = new Card(District.DONJON);
        secondPickedCard = new Card(District.FORTRESS);
        spyPlayer = spy(new RandomBot(2, deck, view));
        otherSpyPlayer = spy(new RandomBot(2, deck, view));
        magician = new Magician();
        magician.setPlayer(spyPlayer);
        spyPlayer.chooseCharacter(new ArrayList<>(List.of(magician)));
    }

    @Test
    void useEffectSwitchHandWithPlayer() {
        ((Magician)spyPlayer.getCharacter()).useEffect(Optional.of(otherSpyPlayer));
        verify(spyPlayer,times(1)).switchHandWith(otherSpyPlayer);
    }

    @Test
    void useEffectSwitchHandWithDeck() {
        when(deck.pick()).thenReturn(firstPickedCard, secondPickedCard);
        spyPlayer.pickACard();

        ((Magician)spyPlayer.getCharacter()).useEffect(Optional.empty());

        assertEquals(1, spyPlayer.getHand().size());
        assertEquals(secondPickedCard, spyPlayer.getHand().get(0));
    }
}



















