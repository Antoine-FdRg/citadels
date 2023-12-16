package com.seinksansdoozebank.fr.model.character.roles;

public enum Role {
    MERCHANT("Marchand"),
    KING("Roi"),
    BISHOP("Evêque"),
    CONDOTTIERE("Condottiere"),
    ARCHITECT("Architecte");
    //MAGICIAN("Magicien"),
    //THIEF("Voleur"),
    //ASSASSIN("Assassin");

    private String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getNbDistrictsCanBeBuild() {
        return this == ARCHITECT ? 3 : 1;
    }

    public int getNbCardToPick() {
        return this == ARCHITECT ? 3 : 1;
    }
}
