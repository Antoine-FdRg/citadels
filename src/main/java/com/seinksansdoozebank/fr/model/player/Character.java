package com.seinksansdoozebank.fr.model.player;


import com.seinksansdoozebank.fr.model.cards.DistrictType;

public abstract class Character {
    private String name;

    public abstract void useEffect();

    public abstract void goldCollectedFromDisctrictType(DistrictType target);
}
