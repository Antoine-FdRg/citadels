package com.seinksansdoozebank.fr.model.character.roles;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Role {
    ASSASSIN("Assassin"),
    THIEF("Voleur"),
    MAGICIAN("Magicien"),
    KING("Roi"),
    BISHOP("Evêque"),
    MERCHANT("Marchand"),
    ARCHITECT("Architecte"),
    CONDOTTIERE("Condottiere");

    @JsonValue
    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getNbDistrictsCanBeBuild() {
        return this == ARCHITECT ? 3 : 1;
    }
}
