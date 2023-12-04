package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DistrictTest {

    private District districtOne;
    private District districtTwo;
    private District districtOneBis;
    private District districtTwoBis;

    @BeforeEach
    void setup() {
        this.districtOne = new District(1);
        this.districtTwo = new District(2);
        this.districtOneBis = new District(1);
        this.districtTwoBis = new District(2, "QuartierBis");
    }

    /**
     * We test de method getName
     */
    @Test
    void getNameTest() {
        assertEquals("Quartier", districtOne.getName());
    }

    /**
     * We test the method getCost
     */
    @Test
    void getCostTest() {
        assertEquals(2, districtTwo.getCost());
    }

    /**
     * We test the method compareTo with different districts
     */
    @Test
    void testCompareToCostWithTwoDifferentCosts() {
        assertEquals(-1, districtOne.compareTo(districtTwo));
        assertNotEquals(0, districtOneBis.compareTo(districtTwo));
    }

    /**
     * We test the method compareTo with same districts
     */
    @Test
    void testCompareToCostWithTheSameDistrict() {
        assertEquals(0, districtOne.compareTo(districtOneBis));
    }

    /**
     * We test our method equal
     */
    @Test
    void testEqualMethod() {
        //With same default name but different cost
        assertFalse(districtOne.equals(districtTwo));
        //With same default name and same cost
        assertTrue(districtOne.equals(districtOneBis));
        //With different name but same name
        assertFalse(districtTwo.equals(districtTwoBis));
    }

}