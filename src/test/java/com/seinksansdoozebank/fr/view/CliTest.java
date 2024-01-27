package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import com.seinksansdoozebank.fr.view.logger.CustomFormatter;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CliTest {
    private Cli view;
    private Player player;
    private Card card;

    @BeforeEach
    void setup() {
        player = mock(Player.class);
        card = mock(Card.class);
        view = mock(Cli.class);
    }

    /**
     * Test if the player plays a card, the view displays the right message
     */
    @Test
    void testDisplayPlayerPlaysCard() {
    }

    @Test
    void testDisplayWinner() {
        view.displayWinner(player);

        verify(view).displayWinner(eq(player));
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
