package com.seinksansdoozebank.fr.model.cards;

public class District implements Comparable<District> {
    int cost;
    String name;

    /**
     * Constructor
     *
     * @param cost the cost to build the district
     * @param name the name of the district
     */
    public District(int cost, String name) {
        this.cost = cost;
        this.name = name;
    }

    /**
     * Constructor with default name
     *
     * @param cost the cost to build the district
     */
    public District(int cost) {
        this(cost, "Quartier");
    }

    /**
     * getter
     *
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
     * We use the cost to compare two districts
     *
     * @param district the object to be compared.
     */

    public int compareTo(District district) {
        return Integer.compare(this.cost, district.cost);
    }


    /**
     * @param obj object to compare
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
        return this.cost * this.name.hashCode();
    }

}