package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public abstract class Character {
    private Player player;
    private final Role role;
    private boolean isDead = false;
    private boolean goldWillBeStolen = false;

    protected Character(Role role) {
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
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Character character)) {
            return false;
        }
        return this.toString().equals(character.toString());
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    public Role getRole() {
        return this.role;
    }

    public abstract void useEffect();

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


    /**
     * setter
     * @param goldWillBeStolen
     */
    public void setGoldWillBeStolen(boolean goldWillBeStolen) {
        this.goldWillBeStolen = goldWillBeStolen;
    }

    /**
     * getter
     * @return a boolean
     */
    public boolean getGoldWillBeStolen() {
        return goldWillBeStolen;
    }


    /**
     * this method decrease the number of gold of the character which is stolen and
     * refreshes the attribute goldWillBeStolen
     * @param players , the list of players of the game
     */
    public void isStolen(List<Player> players) {
        for(Player searchedPlayer : players){
            if(searchedPlayer.getCharacter().role==Role.THIEF){
                //We add the number of gold stolen to the number of gold of the thief
                searchedPlayer.increaseGold(this.getPlayer().getNbGold());
                break;
            }
        }
        this.getPlayer().decreaseGold(this.getPlayer().getNbGold());
        this.setGoldWillBeStolen(false);
    }
}