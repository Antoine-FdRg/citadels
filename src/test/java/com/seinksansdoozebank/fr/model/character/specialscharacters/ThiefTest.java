package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThiefTest {
    private Assassin assassin;
    private Thief thief;
    private Merchant merchant;

    @Mock
    private IView view;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickCoin(15);
        Deck deckThief = new Deck();
        thief = new Thief();
        Player playerThief = new RandomBot(2, deckThief, view);
        thief.setPlayer(playerThief);

        Deck deckAssassin = new Deck();
        assassin = new Assassin();
        Player playerAssassin = new RandomBot(2, deckAssassin, view);
        assassin.setPlayer(playerAssassin);

        Deck deckMerchant = new Deck();
        Player playerMerchant = new RandomBot(2, deckMerchant, view);
        merchant = new Merchant();
        merchant.setPlayer(playerMerchant);
    }

    /**
     * we test the use effet of the thief on a good character
     */
    @Test
    void testUseEffect() {
        thief.useEffect(merchant);
        assertEquals(thief.getPlayer(),merchant.getSavedThief());
    }

    /**
     * we test the use effect of the thief on itself
     */
    @Test
    void testUseEffectWhenCharacterIsAThief() {
        assertThrows(IllegalArgumentException.class, () -> thief.useEffect(thief));
    }

    /**
     * We test the use effect of the thief on a dead character, it must not work
     */
    @Test
    void testUseEffectWhenCharacterIsDead() {
        merchant.kill();
        assertThrows(IllegalStateException.class, () -> thief.useEffect(merchant));
    }

    /**
     * We test the use effect of the thief on the assassin, it must not work
     */
    @Test
    void testUseEffectWhenCharacterIsTheAssassin() {
        assertThrows(IllegalArgumentException.class, () -> thief.useEffect(assassin));
    }


}
