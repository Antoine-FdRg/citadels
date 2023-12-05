package com.seinksansdoozebank.fr.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<District> districtList;
    private static final int NUMBER_OF_CARDS = 65;


    /**
     * Constructor which implements a new deck of 65 districts
     */
    public Deck() {
        this.districtList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            this.districtList.add(new District(random.nextInt(1,5)));
        }
    }


    /**
     * @param index
     * @return the district which is at the specified index in the deck
     * @throws ArrayIndexOutOfBoundsException
     */
    public District getDistrictWithIndex(int index) throws ArrayIndexOutOfBoundsException {
        if (index > districtList.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return districtList.get(index);
    }

    /**
     * getter
     * @return the deck of 65 districts
     */
    public List<District> getDeck() {
        return districtList;
    }

}
