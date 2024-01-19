package com.seinksansdoozebank.fr.cli;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.logging.Level;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CliTest {

    @Mock
    private Player spyPlayer;

    @Mock
    private IView view;

    @Mock
    private Card card;

    @BeforeEach
    void setup() {
        spyPlayer = spy(new RandomBot(10, mock(Deck.class), view));
        view = mock(Cli.class);
        card = mock(Card.class);
    }

    /**
     * Test if the player plays a card, the view displays the right message
     */
    @Test
    void testDisplayPlayerPlaysCard() {
        // Arrange
        when(card.getDistrict()).thenReturn(District.BARRACK);

        // Act
        view.displayPlayerPlaysCard(spyPlayer, card);

        // Assert
        verify(view, times(1)).displayPlayerPlaysCard(spyPlayer, card);
    }

    @Test
    void testDisplayWinner() {
        // TODO
    }

    @Test
    void testDisplayPlayerStartPlaying() {
        // TODO
    }

    @Test
    void testDisplayPlayerPickCards() {
        // TODO
    }

    @Test
    void testDisplayPlayerPicksGold() {
        // TODO
    }

    @Test
    void testDisplayPlayerChooseCharacter() {
        // TODO
    }

    @Test
    void testDisplayPlayerRevealCharacter() {
        // TODO
    }

    @Test
    void testDisplayPlayerDestroyDistrict() {
        // TODO
    }

    @Test
    void testDisplayPlayerScore() {
        // TODO
    }

    @Test
    void testDisplayPlayerGetBonus() {
        // TODO
    }

    @Test
    void testDisplayPlayerUseAssassinEffect() {
        // TODO
    }

    @Test
    void testDisplayPlayerHand() {
        // TODO
    }

    @Test
    void testDisplayPlayerCitadel() {
        // TODO
    }

    @Test
    void testDisplayPlayerInfo() {
        // TODO
    }

    @Test
    void testDisplayUnusedCharacterInRound() {
        // TODO
    }

    @Test
    void testDisplayGameFinished() {
        // TODO
    }

    @Test
    void testDisplayRound() {
        // TODO
    }

    @Test
    void testDisplayPlayerError() {
        // TODO
    }

    @Test
    void testDisplayPlayerStrategy() {
        // TODO
    }

    @Test
    void testDisplayStolenCharacter() {
        // TODO
    }

    @Test
    void testDisplayActualNumberOfGold() {
        // TODO
    }

    @Test
    void testDisplayPlayerUseMagicianEffect() {
        // TODO
    }

}
