package com.seinksansdoozebank.fr.model.cards;

/**
 * The DistrictType enum represents the type of a district
 */
public enum DistrictType {
    /**
     * The district type religion
     */
    RELIGION("Religion"),
    /**
     * The district type nobility
     */
    NOBILITY("Noblesse"),
    /**
     * The district type trade and crafts
     */
    TRADE_AND_CRAFTS("Commerce et artisanat"),
    /**
     * The district type soldiery
     */
    SOLDIERLY("Soldatesque"),
    /**
     * The district type prestige
     */
    PRESTIGE("Prestige");

    private final String name;

    /**
     * DistrictType constructor
     *
     * @param name the name of the districtType
     */
    DistrictType(String name) {
        this.name = name;
    }

    /**
     * @return the name of the districtType
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of the districtType
     * @return the district type that contains name or null if not
     */
    public static DistrictType getDistrictTypeByString(String name) {
        for (DistrictType districtType : DistrictType.values()) {
            if (name.equals(districtType.getName())) {
                return districtType;
            }
        }
        return null;
    }

    @Override
    public String toString() {

        return this.name;
    }
}
