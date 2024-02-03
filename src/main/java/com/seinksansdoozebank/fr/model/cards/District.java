package com.seinksansdoozebank.fr.model.cards;

import com.seinksansdoozebank.fr.model.cards.effect.ActiveEffect;
import com.seinksansdoozebank.fr.model.cards.effect.LaboratoryEffect;
import com.seinksansdoozebank.fr.model.cards.effect.ManufactureEffect;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

public enum District implements Comparable<District> {

    TEMPLE("Temple", DistrictType.RELIGION, 1, 3, null),
    CHURCH("Eglise", DistrictType.RELIGION, 2, 4, null),
    MONASTERY("Monastère", DistrictType.RELIGION, 3, 3, null),
    CATHEDRAL("Cathédrale", DistrictType.RELIGION, 5, 2, null),
    MANOR("Manoir", DistrictType.NOBILITY, 3, 5, null),
    CASTLE("Château", DistrictType.NOBILITY, 4, 4, null),
    PALACE("Palais", DistrictType.NOBILITY, 5, 2, null),
    TAVERN("Taverne", DistrictType.TRADE_AND_CRAFTS, 1, 5, null),
    CORNER_SHOP("Echoppe", DistrictType.TRADE_AND_CRAFTS, 2, 3, null),
    MARKET_PLACE("Marché", DistrictType.TRADE_AND_CRAFTS, 2, 4, null),
    TRADING_POST("Comptoir", DistrictType.TRADE_AND_CRAFTS, 3, 3, null),
    PORT("Port", DistrictType.TRADE_AND_CRAFTS, 4, 3, null),
    TOWN_HALL("Hôtel de ville", DistrictType.TRADE_AND_CRAFTS, 5, 2, null),
    WATCH_TOWER("Tour de guet", DistrictType.SOLDIERLY, 1, 3, null),
    JAIL("Prison", DistrictType.SOLDIERLY, 2, 3, null),
    BARRACK("Caserne", DistrictType.SOLDIERLY, 3, 3, null),
    FORTRESS("Forteresse", DistrictType.SOLDIERLY, 5, 2, null),
    COURTYARD_OF_MIRACLE("Cour des miracles", DistrictType.PRESTIGE, 2, 1, null),
    DONJON("Donjon", DistrictType.PRESTIGE, 3, 2, null),
    LABORATORY("Laboratoire", DistrictType.PRESTIGE, 5, 1, new LaboratoryEffect()),
    MANUFACTURE("Manufacture", DistrictType.PRESTIGE, 5, 1, new ManufactureEffect()),
    OBSERVATORY("Observatoire", DistrictType.PRESTIGE, 5, 1, null),
    CEMETERY("Cimetière", DistrictType.PRESTIGE, 1, 1, null),
    LIBRARY("Bibliothèque", DistrictType.PRESTIGE, 6, 1, null),
    SCHOOL_OF_MAGIC("Ecole de magie", DistrictType.PRESTIGE, 6, 1, null),
    UNIVERSITY("Université", DistrictType.PRESTIGE, 6, 1, null),
    PORT_FOR_DRAGONS("Dracoport", DistrictType.PRESTIGE, 6, 1, null);

    private final String name;
    private final DistrictType districtType;
    private final int cost;
    private final int numberOfAppearance;
    private final ActiveEffect activeEffect;

    District(String name, DistrictType districtType, int cost, int numberOfAppearance, ActiveEffect activeEffect) {
        this.name = name;
        this.districtType = districtType;
        this.cost = cost;
        this.numberOfAppearance = numberOfAppearance;
        this.activeEffect = activeEffect;
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
     *
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

    public void useActiveEffect(Player player, IView view) {
        if (this.activeEffect != null) {
            this.activeEffect.use(player, view);
        }
    }

    public ActiveEffect getActiveEffect() {
        return this.activeEffect;
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
    public String toString() {
        return "Le/la : " + this.name + ", son type : " + this.districtType + ", son coût :  " + this.cost + " pièces d''or";
    }
}
