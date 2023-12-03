package com.seinksansdoozebank.fr.model.cards;

public class District implements Comparable<District> {
    int cost;
    String name;

    /**
     * Constructor
     * @param cost
     * @param name
     */
    public District(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }

    /**
     * Constructor with default name
     * @param cost
     */
    public District(int cost) {
        this(cost, "Quartier");
    }

    /**
     * getter
     * @return cost
     */
    public int getCost() {
        return cost;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param district the object to be compared.
     */

    public int compareToCost(District district) {
        return Integer.compare(this.cost, district.cost);
    }

    /**
     * @param district the object to be compared.
     */
    @Override
    public int compareTo(District district) {
        //TODO must be changed in the future
        return Integer.compare(this.cost, district.cost);
    }

    /**
     * @param obj
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof District district) {
            return this.compareTo(district) == 0 && this.name.equals(district.name);
        }
        return false;
    }

    /**
     * @return hascode
     */
    public int hashCode() {
        return this.cost;
    }

}