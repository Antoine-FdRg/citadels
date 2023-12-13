package com.seinksansdoozebank.fr.model.cards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


class DistrictTest {

    @BeforeEach
    void setup() {
    }

    /**
     * We test de method getName
     */
    @Test
    void getNameTest() {
        assertEquals("Palais", District.PALACE.getName());
    }

    /**
     * We test the method getCost
     */
    @Test
    void getCostTest() {
        assertEquals(4, District.PORT.getCost());
    }

    /**
     * We test the method getDistrictType
     */
    @Test
    void getDistrictTypeTest() {
        assertEquals(DistrictType.TRADE_AND_CRAFTS, District.PORT.getDistrictType());
    }

    /**
     * We test the method getNumberOfAppearance
     */
    @Test
    void getNumberOfAppearanceTest() {
        assertEquals(2, District.TOWN_HALL.getNumberOfAppearance());
    }

    /**
     * We test our method getDistrictWithName
     */
    @Test
    void getDistrictWithNameTest() {

        String nameValid = "Port";
        String nameInvalid = "NeCorrespondARien";

        assertEquals(District.PORT, District.getDistrictWithName(nameValid));
        assertNull(District.getDistrictWithName(nameInvalid));
    }

    /**
     * We test our method getDistrictWithOrdinal
     */
    @Test
    void getDistrictWithOrdinalTest() {
        assertEquals(District.LIBRARY, District.getDistrictByOrdinal(23));
        assertNull(District.getDistrictByOrdinal(27));
    }

    /**
     * We test our method toString
     */
    @Test
    void toStringTest() {
        assertEquals("Le/a : Port, son type : Commerce et artisanat, son coût :  4 pièces d'or", District.PORT.toString());
    }
}