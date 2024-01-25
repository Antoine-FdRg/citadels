package com.seinksansdoozebank.fr.model.cards;

import com.seinksansdoozebank.fr.model.bank.Bank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card monasteryCard;
    private Card monasteryCardBis;
    private Card barrackCard;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickCoin(Bank.MAX_COIN / 2);
        monasteryCard = new Card(District.MONASTERY);
        barrackCard = new Card(District.BARRACK);
        monasteryCardBis = new Card(District.MONASTERY);
    }

    @Test
    void equalsTest() {
        assertNotEquals(monasteryCard, barrackCard);
        assertEquals(monasteryCard, monasteryCardBis);
    }

    @Test
    void equalsTestOnNull() {
        assertNotEquals(null, monasteryCard);
    }
}