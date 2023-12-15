package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    private Card monasteryCard;
    private Card monasteryCardBis;
    private Card barrackCard;

    @BeforeEach
    void setUp() {
        monasteryCard = new Card(District.MONASTERY);
        barrackCard = new Card(District.BARRACK);
        monasteryCardBis = new Card(District.MONASTERY);
    }

    @Test
    void equalsTest() {
        assertFalse(monasteryCard.equals(barrackCard));
        assertTrue(monasteryCard.equals(monasteryCardBis));
    }
}