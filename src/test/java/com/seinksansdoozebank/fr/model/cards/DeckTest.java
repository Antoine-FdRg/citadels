package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {

    private Deck districtList;
    private Deck districtListTest;

    @BeforeEach
    void setup() {
        districtList = new Deck();
        districtListTest = new Deck();
    }

    /**
     * On vérifie que le deck possède bien 65 cartes quartiers lors de sa création
     */
    @Test
    void deckCreationTest() {
        assertEquals(65, districtList.getDeck().size());
    }

    /**
     * On vérifie que le coût attribué à chaque quartier est compris entre 1 et 5
     */
    @Test
    void getDistrictCostWIthDeckTest() {
        for (int i = 0; i < 65; i++) {
            assertTrue(districtList.getDeck().get(i).getCost() >= 1);
            assertTrue(districtList.getDeck().get(i).getCost() <= 5);
        }
    }

    /**
     * We verify when the list of districts is shuffled that it has differences with the one which is not shuffle.
     */
    @Test
    void shuffleTest() {
        districtListTest.shuffle();
        int differencesCount = 0;
        for (int i = 0; i < 65; i++) {
            if (districtListTest.getDeck().get(i).compareTo(districtList.getDeck().get(i)) != 0) {
                differencesCount++;
            }
        }
        assertTrue(differencesCount > 0);
    }

    /**
     * We verify that if the list is empty, the deck is built again
     * This method will be improved with the milestone 2
     */
    @Test
    void pickDistrictTest() {
        for (int i = 0; i < 65; i++) {
            districtList.pick();
        }
        districtList.pick();
        assertEquals(64, districtList.getDeck().size());
    }
}
