package com.seinksansdoozebank.fr.model.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<District> deck;
    private static final int NUMBER_OF_CARDS = 65;


    /**
     * Constructor which implements a new deck of 65 districts
     */
    public Deck() {
        this.deck = new ArrayList<>();
        Random random = new Random();
        int cost;
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            cost = 1 + random.nextInt(5 - 1);
            this.deck.add(new District(cost));
        }
    }


    /**
     * @param index
     * @return the district which is at the specified index in the deck
     * @throws ArrayIndexOutOfBoundsException
     */
    public District getDistrictWithIndex(int index) throws ArrayIndexOutOfBoundsException {
        if (index > deck.size()) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return deck.get(index);
    }

    /**
     * getter
     * @return the deck of 65 districts
     */
    public List<District> getDeck() {
        return deck;
    }

}
