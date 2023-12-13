package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardTest {
    private Card monasteryCard;
    private Card monasteryCardBis;
    private Card stationCard;

    @BeforeEach
    void setUp() {
        monasteryCard = new Card(District.MONASTERY);
        stationCard = new Card(District.STATION);
        monasteryCardBis = new Card(District.MONASTERY);
    }

    /**
     * We test if it gives us the right district
     */
    @Test
    void getDistrictTest() {
        assertEquals(District.MONASTERY, monasteryCard.getDistrict());
    }

    /**
     * We test if its gives us the right cost of the card
     */
    @Test
    void getCostOfTheCardTest(){
        assertEquals(3,monasteryCard.getCostOfTheCard());
    }
    @Test
    void equalsTest() {
        assertFalse(monasteryCard.equals(stationCard));
        assertTrue(monasteryCard.equals(monasteryCardBis));
    }
}