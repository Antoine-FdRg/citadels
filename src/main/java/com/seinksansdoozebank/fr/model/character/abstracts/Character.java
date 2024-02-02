package com.seinksansdoozebank.fr.model.character.abstracts;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

public abstract class Character {
    @JsonIgnore
    private Player player;
    @JsonProperty
    private final Role role;
    @JsonIgnore
    private boolean isDead;
    @JsonProperty
    private Player savedThief ;

    protected Character(Role role) {
        this.isDead = false;
        this.role = role;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof Character character) && this.toString().equals(character.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
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

    @JsonProperty
    public boolean isDead() {
        return this.isDead;
    }
    public void resurrect() {
        this.isDead = false;
    }

    /**
     * @param player the player to steal
     */
    public void setSavedThief(Player player) {
        savedThief=player;
    }


    /**
     * getter
     * @return a player
     */
    public Player getSavedThief() {
        return savedThief;
    }


    /**
     * this method decrease the number of gold of the character which is stolen and
     * refreshes the attribute goldWillBeStolen
     */
    public void isStolen() {
        //We add the number of gold stolen to the number of gold of the thief
        getSavedThief().increaseGold(this.getPlayer().getNbGold());
        this.getPlayer().decreaseGold(this.getPlayer().getNbGold());
        this.setSavedThief(null);
    }
}