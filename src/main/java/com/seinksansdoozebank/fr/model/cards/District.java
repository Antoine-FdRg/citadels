package com.seinksansdoozebank.fr.model.cards;

import com.seinksansdoozebank.fr.model.cards.effect.ActiveEffect;
import com.seinksansdoozebank.fr.model.cards.effect.LaboratoryEffect;
import com.seinksansdoozebank.fr.model.cards.effect.ManufactureEffect;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;

/**
 * The District class represents a district card
 */
public enum District implements Comparable<District> {
    /**
     * District "Temple". Type : Religion, Cost : 1, number of appearance : 3.
     */
    TEMPLE("Temple", DistrictType.RELIGION, 1, 3, null),

    /**
     * District "Eglise". Type : Religion, Cost : 2, number of appearance : 4.
     */
    CHURCH("Eglise", DistrictType.RELIGION, 2, 4, null),

    /**
     * District "Monastère". Type : Religion, Cost : 3, number of appearance : 3.
     */
    MONASTERY("Monastère", DistrictType.RELIGION, 3, 3, null),

    /**
     * District "Cathédrale". Type : Religion, Cost : 5, number of appearance : 2.
     */
    CATHEDRAL("Cathédrale", DistrictType.RELIGION, 5, 2, null),

    /**
     * District "Manoir". Type : Noblesse, Cost : 3, number of appearance : 5.
     */
    MANOR("Manoir", DistrictType.NOBILITY, 3, 5, null),

    /**
     * District "Château". Type : Noblesse, Cost : 4, number of appearance : 4.
     */
    CASTLE("Château", DistrictType.NOBILITY, 4, 4, null),

    /**
     * District "Palais". Type : Noblesse, Cost : 5, number of appearance : 2.
     */
    PALACE("Palais", DistrictType.NOBILITY, 5, 2, null),

    /**
     * District "Taverne". Type : Commerce et Artisanat, Cost : 1, number of appearance : 5.
     */
    TAVERN("Taverne", DistrictType.TRADE_AND_CRAFTS, 1, 5, null),

    /**
     * District "Echoppe". Type : Commerce et Artisanat, Cost : 2, number of appearance : 3.
     */
    CORNER_SHOP("Echoppe", DistrictType.TRADE_AND_CRAFTS, 2, 3, null),

    /**
     * District "Marché". Type : Commerce et Artisanat, Cost : 2, number of appearance : 4.
     */
    MARKET_PLACE("Marché", DistrictType.TRADE_AND_CRAFTS, 2, 4, null),

    /**
     * District "Comptoir". Type : Commerce et Artisanat, Cost : 3, number of appearance : 3.
     */
    TRADING_POST("Comptoir", DistrictType.TRADE_AND_CRAFTS, 3, 3, null),

    /**
     * District "Port". Type : Commerce et Artisanat, Cost : 4, number of appearance : 3.
     */
    PORT("Port", DistrictType.TRADE_AND_CRAFTS, 4, 3, null),

    /**
     * District "Hôtel de ville". Type : Commerce et Artisanat, Cost : 5, number of appearance : 2.
     */
    TOWN_HALL("Hôtel de ville", DistrictType.TRADE_AND_CRAFTS, 5, 2, null),

    /**
     * District "Tour de guet". Type : Militaire, Cost : 1, number of appearance : 3.
     */
    WATCH_TOWER("Tour de guet", DistrictType.SOLDIERLY, 1, 3, null),

    /**
     * District "Prison". Type : Militaire, Cost : 2, number of appearance : 3.
     */
    JAIL("Prison", DistrictType.SOLDIERLY, 2, 3, null),

    /**
     * District "Caserne". Type : Militaire, Cost : 3, number of appearance : 3.
     */
    BARRACK("Caserne", DistrictType.SOLDIERLY, 3, 3, null),

    /**
     * District "Forteresse". Type : Militaire, Cost : 5, number of appearance : 2.
     */
    FORTRESS("Forteresse", DistrictType.SOLDIERLY, 5, 2, null),

    /**
     * District "Cour des miracles". Type : Prestige, Cost : 2, number of appearance : 1.
     */
    COURTYARD_OF_MIRACLE("Cour des miracles", DistrictType.PRESTIGE, 2, 1, null),

    /**
     * District "Donjon". Type : Prestige, Cost : 3, number of appearance : 2.
     */
    DONJON("Donjon", DistrictType.PRESTIGE, 3, 2, null),

    /**
     * District "Laboratoire". Type : Prestige, Cost : 5, number of appearance : 1. Effet spécial grâce à l'objet {@link LaboratoryEffect}.
     */
    LABORATORY("Laboratoire", DistrictType.PRESTIGE, 5, 1, new LaboratoryEffect()),

    /**
     * District "Manufacture". Type : Prestige, Cost : 5, number of appearance : 1. Effet spécial grâce à l'objet {@link ManufactureEffect}.
     */
    MANUFACTURE("Manufacture", DistrictType.PRESTIGE, 5, 1, new ManufactureEffect()),

    /**
     * District "Observatoire". Type : Prestige, Cost : 5, number of appearance : 1.
     */
    OBSERVATORY("Observatoire", DistrictType.PRESTIGE, 5, 1, null),

    /**
     * District "Cimetière". Type : Prestige, Cost : 5, number of appearance : 1.
     */
    CEMETERY("Cimetière", DistrictType.PRESTIGE, 5, 1, null),

    /**
     * District "Bibliothèque". Type : Prestige, Cost : 6, number of appearance : 1.
     */
    LIBRARY("Bibliothèque", DistrictType.PRESTIGE, 6, 1, null),

    /**
     * District "Ecole de magie". Type : Prestige, Cost : 6, number of appearance : 1.
     */
    SCHOOL_OF_MAGIC("Ecole de magie", DistrictType.PRESTIGE, 6, 1, null),

    /**
     * District "Université". Type : Prestige, Cost : 6, number of appearance : 1.
     */
    UNIVERSITY("Université", DistrictType.PRESTIGE, 6, 1, null),

    /**
     * District "Dracoport". Type : Prestige, Cost : 6, number of appearance : 1.
     */
    PORT_FOR_DRAGONS("Dracoport", DistrictType.PRESTIGE, 6, 1, null);

    private final String name;
    private final DistrictType districtType;
    private final int cost;
    private final int numberOfAppearance;
    private final ActiveEffect activeEffect;

    /**
     * District constructor
     *
     * @param name               the name of the district
     * @param districtType       the type of the district
     * @param cost               the cost of construction of one district
     * @param numberOfAppearance the number of appearances of the district
     * @param activeEffect       the active effect of the district
     */
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

    /**
     * Use the active effect of the district
     * @param player the player
     * @param view the view
     */
    public void useActiveEffect(Player player, IView view) {
        if (this.activeEffect != null) {
            this.activeEffect.use(player, view);
        }
    }

    /**
     * Get the active effect of the district
     * @return the active effect
     */
    public ActiveEffect getActiveEffect() {
        return this.activeEffect;
    }

    /**
     * Get the number of appearances of the district
     *
     * @return the number of appearances of district
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
