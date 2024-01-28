package com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CardChoosingStrategyTest {

    CardChoosingStrategy cardChoosingStrategy;
    Player mockPlayer;

    IView mockView;

    @BeforeEach
    void setUp() {
        mockPlayer = mock(Player.class);
        cardChoosingStrategy = new CardChoosingStrategy();
        mockView = mock(IView.class);
    }

    @Test
    void testApplyReturnCorrespondingCard() {
        // Create a list of cards
        List<Card> cards = new ArrayList<>(
                List.of(
                        new Card(District.TEMPLE), // 1
                        new Card(District.CHURCH), // 2
                        new Card(District.MONASTERY) // 3
                )
        );
        when(mockPlayer.getNbGold()).thenReturn(10);
        when(mockPlayer.canPlayCard(any())).thenReturn(true);
        when(mockPlayer.getHand()).thenReturn(cards);
        Optional<Card> cardChoosen = this.cardChoosingStrategy.apply(mockPlayer);
        assertTrue(cardChoosen.isPresent());
        assertEquals(cards.get(1), cardChoosen.get());
    }

    @Test
    void testApplyReturnCorrespondingCardWithAllSameCost() {
        // Create a list of cards
        List<Card> cards = new ArrayList<>(
                List.of(
                        new Card(District.TEMPLE), // 1
                        new Card(District.TAVERN), // 1
                        new Card(District.WATCH_TOWER) // 1
                )
        );
        when(mockPlayer.getNbGold()).thenReturn(10);
        when(mockPlayer.canPlayCard(any())).thenReturn(true);
        when(mockPlayer.getHand()).thenReturn(cards);
        Optional<Card> cardChoosen = this.cardChoosingStrategy.apply(mockPlayer);
        assertTrue(cardChoosen.isPresent());
        assertEquals(cards.get(0), cardChoosen.get());
    }

    @Test
    void testApplyReturnEmptyCardBecauseEmptyHand() {
        // Create a list of cards
        List<Card> cards = new ArrayList<>();
        when(mockPlayer.getNbGold()).thenReturn(10);
        when(mockPlayer.getHand()).thenReturn(cards);
        Optional<Card> cardChoosen = this.cardChoosingStrategy.apply(mockPlayer);
        assertTrue(cardChoosen.isEmpty());
    }
}