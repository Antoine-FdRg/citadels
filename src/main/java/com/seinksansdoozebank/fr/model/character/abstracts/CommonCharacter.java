package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public abstract class CommonCharacter extends Character {
    private final Role role;
    private final DistrictType target;
    private Player player;

    /**
     * Set the player of the character
     *
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    protected CommonCharacter(Role role, DistrictType target) {
        this.role = role;
        this.target = target;
    }

    public abstract void useEffect();
    public abstract void useEffect(Character character);
    public abstract void useEffect(Character character, District district);

    /**
     * For each district in the citadel of the target type, the player will collect one gold
     */
    public void goldCollectedFromDisctrictType() {
        int nbGold = 0;
        for (Card card : this.player.getCitadel()) {
            if (card.getDistrict().getDistrictType() == target) {
                nbGold++;
            }
        }
        this.player.increaseGold(nbGold);
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public String toString() {
        return this.role.getName();
    }
}
