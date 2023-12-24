package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

public abstract class Character {
    private Player player;
    private final Role role;
    private boolean isDead = false;

    protected Character(Role role) {
        this.role = role;
    }

    public abstract void useEffect();
    public abstract void useEffect(Character character);
    public abstract void useEffect(Character character, District district);

    /**
     * Set the player of the character
     *
     * @param player the player to set
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Role getRole() {
        return this.role;
    }

    @Override
    public String toString() {
        return this.role.getName();
    }

    /**
     * Kill the character
     */
    public void kill() {
        this.isDead = true;
    }

    public boolean isDead() {
        return this.isDead;
    }
}
