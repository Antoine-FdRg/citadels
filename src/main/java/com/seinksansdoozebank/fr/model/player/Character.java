package com.seinksansdoozebank.fr.model.player;


public abstract class Character {
    private String name;

    public abstract void useEffect();

    public abstract void goldCollectedFromDisctrictType(DistrictType target);
}
