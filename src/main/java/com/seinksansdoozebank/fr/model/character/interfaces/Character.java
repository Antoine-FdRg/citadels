package com.seinksansdoozebank.fr.model.character.interfaces;


import com.seinksansdoozebank.fr.model.cards.DistrictType;

public interface Character {

    /**
     * Method called to perform the action of the character
     * This method will ask the user to choose an action to perform
     * The action will be performed by the character
     */
    void performAction();
}
