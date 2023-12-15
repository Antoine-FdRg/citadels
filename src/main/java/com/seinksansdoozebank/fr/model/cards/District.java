package com.seinksansdoozebank.fr.model.cards;

public enum District implements Comparable<District> {

    TEMPLE("Temple", DistrictType.RELIGION, 1, 3),
    CHURCH("Eglise", DistrictType.RELIGION, 2, 4),
    MONASTERY("Monastère", DistrictType.RELIGION, 3, 3),
    CATHEDRAL("Cathédrale", DistrictType.RELIGION, 5, 2),
    MANOR("Manoir", DistrictType.NOBILITY, 3, 5),
    CASTLE("Château", DistrictType.NOBILITY, 4, 4),
    PALACE("Palais", DistrictType.NOBILITY, 5, 2),
    TAVERN("Taverne", DistrictType.TRADE_AND_CRAFTS, 1, 5),
    CORNER_SHOP("Echoppe", DistrictType.TRADE_AND_CRAFTS, 2, 3),
    MARKET_PLACE("Marché", DistrictType.TRADE_AND_CRAFTS, 2, 4),
    FACTORY("Comptoir", DistrictType.TRADE_AND_CRAFTS, 3, 3),
    PORT("Port", DistrictType.TRADE_AND_CRAFTS, 4, 3),
    TOWN_HALL("Hôtel de ville", DistrictType.TRADE_AND_CRAFTS, 5, 2),
    WATCH8TOWER("Tour de guet", DistrictType.SOLDIERLY, 1, 3),
    JAIL("Prison", DistrictType.SOLDIERLY, 2, 3),
    STATION("Caserne", DistrictType.SOLDIERLY, 3, 3),
    FORTRESS("Forteresse", DistrictType.SOLDIERLY, 5, 2),
    COURTYARD_OF_MIRACLE("Cour des miracles", DistrictType.PRESTIGE, 2, 1),
    DONJON("Donjon", DistrictType.PRESTIGE, 3, 2),
    LABORATORY("Laboratoire", DistrictType.PRESTIGE, 5, 1),
    MANUFACTURE("Manufacture", DistrictType.PRESTIGE, 5, 1),
    OBSERVATORY("Observatoire", DistrictType.PRESTIGE, 5, 1),
    CEMETERY("Cimetière", DistrictType.PRESTIGE, 5, 1),
    LIBRARY("Bibliothèque", DistrictType.PRESTIGE, 6, 1),
    SCHOOL_OF_MAGIC("Ecole de magie", DistrictType.PRESTIGE, 6, 1),
    UNIVERSITY("Université", DistrictType.PRESTIGE, 6, 1),
    PORT_FOR_DRAGONS("Dracoport", DistrictType.PRESTIGE, 6, 1);

    private final String name;
    private final DistrictType districtType;
    private final int cost;
    private final int numberOfAppearance;


    District(String name, DistrictType districtType, int cost, int numberOfAppearance) {
        this.name = name;
        this.districtType = districtType;
        this.cost = cost;
        this.numberOfAppearance = numberOfAppearance;
    }

    /**
     * @return the name of the district
     */
    public String getName() {
        return name;
    }

    /**
     * @param name of the district
     * @return the district which corresponds to the string given in parameters
     */
    public static District getDistrictWithName(String name) {
        for (District district : District.values()) {
            if (name.equals(district.getName())) {
                return district;
            }
        }
        return null;
    }

    /**
     * getter
     * @param ordinal of the enum
     * @return the district which corresponds to the ordinal given in parameters
     */
    public static District getDistrictByOrdinal(int ordinal) {
        for (District district : District.values()) {
            if (district.ordinal() == ordinal) {
                return district;
            }
        }
        return null;
    }

    /**
     * getter
     *
     * @return the district Type
     */
    public DistrictType getDistrictType() {
        return districtType;
    }

    /**
     * getter
     *
     * @return the cost of construction of one district
     */
    public int getCost() {
        return cost;
    }

    /**
     * getter
     *
     * @return the number of appearances of one district card
     */
    public int getNumberOfAppearance() {
        return numberOfAppearance;
    }

    /**
     * @return the representation of a district
     */
    @Override
    public String toString(){
        return "Le/la : "+ this.name + ", son type : " + this.districtType+ ", son coût :  "+this.cost+" pièces d'or";
    }
}
