package com.seinksansdoozebank.fr.model.cards.effect;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ActiveEffectTest {

    Player spyPlayer;
    IView view;
    Deck deck;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(Cli.class);
        deck = spy(new Deck());
        spyPlayer = spy(new RandomBot(10, deck, view));
    }

    @Test
    void useLaboratoryEffect() {
        Random mockRandom = mock(Random.class);
        spyPlayer.getHand().add(new Card(District.TEMPLE));

        when(mockRandom.nextInt(spyPlayer.getHand().size())).thenReturn(0);
        ((RandomBot) spyPlayer).setRandom(mockRandom);

        // give to the player a card in his hand
        Card laboratory = new Card(District.LABORATORY);
        laboratory.getDistrict().useActiveEffect(spyPlayer, view);
        assertEquals(11, spyPlayer.getNbGold());
    }

    @Test
    void useManufactureEffect() {
        Random mockRandom = mock(Random.class);
        when(mockRandom.nextBoolean()).thenReturn(true);
        ((RandomBot) spyPlayer).setRandom(mockRandom);

        Card manufacture = new Card(District.MANUFACTURE);
        manufacture.getDistrict().useActiveEffect(spyPlayer, view);
        assertEquals(7, spyPlayer.getNbGold());
    }
}