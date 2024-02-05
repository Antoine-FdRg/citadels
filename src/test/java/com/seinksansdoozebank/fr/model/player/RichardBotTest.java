package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

class RichardBotTest {
    RichardBot richardBot;
    IView view;
    Deck deck;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN / 2);
        view = mock(Cli.class);
        deck = spy(new Deck());
        richardBot = spy(new RichardBot(10, deck, view));
    }

}