package com.seinksansdoozebank.fr.model.cards;

import com.fasterxml.jackson.annotation.JsonValue;

public enum DistrictType {
    RELIGION("Religion"),
    NOBILITY("Noblesse"),
    TRADE_AND_CRAFTS("Commerce et artisanat"),
    SOLDIERLY("Soldatesque"),
    PRESTIGE("Prestige");
    @JsonValue
    private final String name;

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
