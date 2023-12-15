package com.seinksansdoozebank.fr.model.character.roles;

public enum Role {
    MERCHANT("Marchand"),
    KING("Roi"),
    BISHOP("Evêque"),
    CONDOTTIERE("Condottiere");

    //ARCHITECT("Architecte"),
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
    }
