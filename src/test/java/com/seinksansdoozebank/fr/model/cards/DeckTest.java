package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {

    private Deck districtList;
    @BeforeEach
    void setup(){
        districtList= new Deck();
    }

    /**
     * On vérifie que le deck possède bien 65 cartes quartiers lors de sa création
     */
    @Test
    void deckCreationTest(){
        assertEquals(65,districtList.getDeck().size());
    }

    /**
     * On vérifie que le coût attribué à chaque quartier est compris entre 1 et 5
     */
    @Test
    void getDistrictCostWIthDeckTest(){
        for(int i=0;i<65;i++){
            assertTrue(districtList.getDistrictWithIndex(i).getCost()>=1);
            assertTrue(districtList.getDistrictWithIndex(i).getCost()<=5);
        }
    }
}
