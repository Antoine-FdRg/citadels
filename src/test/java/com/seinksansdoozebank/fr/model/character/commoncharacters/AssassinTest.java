package com.seinksansdoozebank.fr.model.character.commoncharacters;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AssassinTest {

    private Assassin assassin;
    private Bishop bishop;

    @BeforeEach
    void setUp() {
        IView view = new Cli();
        Deck deckAssassin = new Deck();
        assassin = new Assassin();
        Player playerAssassin = new RandomBot(2, deckAssassin, view);
        assassin.setPlayer(playerAssassin);
        Deck deckBishop = new Deck();
        Player playerBishop = new RandomBot(2, deckBishop, view);
        bishop = new Bishop();
        bishop.setPlayer(playerBishop);
    }

    /**
     * Check if the bishop is dead after the assassin's effect.g
     */
    @Test
    void testUseEffect() {
        // Test the effect of the assassin.
        assassin.useEffect(bishop);
        // Check if the bishop is dead.
        assertTrue(bishop.isDead());
    }
}
