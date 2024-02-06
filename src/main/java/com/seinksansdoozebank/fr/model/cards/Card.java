package com.seinksansdoozebank.fr.model.cards;

public class Card {
    private final District district;
    private static int counter = 1;
    protected final int id;

    /**
     * Constructor
     *
     * @param district from class District
     */
    public Card(District district) {
        this.district = district;
        this.id = counter;
        counter++;
    }

    public static void resetCounterId() {
        counter = 1;
    }

    /**
     * getter
     *
     * @return the district of the card
     */
    public District getDistrict() {
        return district;
    }

    /**
     * @param obj an object
     * @return a boolean true if it is the same object, false in the other case
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Card card) {
            return district.equals(card.district);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

    /**
     * @return a string which is the representation of a card associated to the toStringMethod of a district
     */
    @Override
    public String toString() {
        return "la carte " + district.toString();
    }
}
