package com.seinksansdoozebank.fr.model.character.roles;

/**
 * The role of a character
 */
public enum Role {
    /**
     * The assassin role
     */
    ASSASSIN("Assassin"),
    /**
     * The thief role
     */
    THIEF("Voleur"),
    /**
     * The magician role
     */
    MAGICIAN("Magicien"),
    /**
     * The king role
     */
    KING("Roi"),
    /**
     * The bishop role
     */
    BISHOP("Evêque"),
    /**
     * The merchant role
     */
    MERCHANT("Marchand"),
    /**
     * The architect role
     */
    ARCHITECT("Architecte"),
    /**
     * The warlord role
     */
    WARLORD("Warlord");

    private final String name;

    /**
     * Role constructor
     *
     * @param name the name of the role
     */
    Role(String name) {
        this.name = name;
    }

    /**
     * Get the name of the role
     * @return the name of the role
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the number of districts that can be built
     * @return the number of districts that can be built
     */
    public int getNbDistrictsCanBeBuild() {
        return this == ARCHITECT ? 3 : 1;
    }
}
