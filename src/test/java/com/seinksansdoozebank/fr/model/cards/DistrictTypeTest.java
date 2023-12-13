package com.seinksansdoozebank.fr.model.cards;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DistrictTypeTest {

    /**
     * We test our method getDistrictByString
     */
    @Test
    void getDistrictTypeByNameTest(){

        String nameValid="Religion";
        String nameInvalid="NeCorrespondARien";

        assertEquals(DistrictType.RELIGION,DistrictType.getDistrictTypeByString(nameValid));
        assertNull(DistrictType.getDistrictTypeByString(nameInvalid));
    }

    /**
     * We test our method toString
     */
    @Test
    void toStringTest(){
        assertEquals("Religion",DistrictType.RELIGION.toString());
    }


}