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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MagicianTest {

    Player spyPlayer;
    Player otherSpyPlayer;
    Magician magician;
    Deck deck;
    Cli view;
    Card firstPickedCard;
    Card secondPickedCard;

    Card firstExchangeCard;
    Card secondExchangeCard;

    @BeforeEach
    void setUp() {
        view = mock(Cli.class);
        deck = mock(Deck.class);
        firstPickedCard = new Card(District.DONJON);
        secondPickedCard = new Card(District.FORTRESS);
        firstExchangeCard = new Card(District.CEMETERY);
        secondExchangeCard = new Card(District.CASTLE);
        spyPlayer = spy(new RandomBot(2, deck, view));
        otherSpyPlayer = spy(new RandomBot(2, deck, view));
        magician = new Magician();
        magician.setPlayer(spyPlayer);
        spyPlayer.chooseCharacter(new ArrayList<>(List.of(magician)));
    }

    @Test
    void useEffectSwitchHandWithPlayer() {
        List<Card> handSave = spyPlayer.getHand();
        List<Card> otherHandSave = otherSpyPlayer.getHand();
        ((Magician) spyPlayer.getCharacter()).useEffect(otherSpyPlayer, null);
        verify(spyPlayer, times(1)).switchHandWith(otherSpyPlayer);
        // Check that the other player has the same hand as the ancient hand spyPlayer
        assertEquals(handSave, otherSpyPlayer.getHand());
        assertEquals(otherHandSave, spyPlayer.getHand());
    }

    @Test
    void useEffectSwitchHandWithDeck() {
        when(deck.pick()).thenReturn(firstPickedCard, secondPickedCard);
        spyPlayer.pickACard();

        // Set the player hand to the firstExchangeCard and secondExchangeCard
        spyPlayer.getHand().add(firstExchangeCard);
        spyPlayer.getHand().add(secondExchangeCard);

        assertEquals(3, spyPlayer.getHand().size());

        ((Magician) spyPlayer.getCharacter()).useEffect(null, List.of(firstExchangeCard, secondExchangeCard));

        assertEquals(3, spyPlayer.getHand().size());
        assertEquals(firstPickedCard, spyPlayer.getHand().get(0));
    }
}



















