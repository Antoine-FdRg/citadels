package com.seinksansdoozebank.fr.model.character.specialscharacters;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Optional;

public class Magician extends Character {

    public Magician() {
        super(Role.MAGICIAN);
    }

    /**
     * Switch the hand of the player with the hand of the target player
     * If the target player is not present, the player will switch his hand with the deck
     *
     * @param optTargetPlayer the target player to switch hand with
     */
    public void useEffect(Optional<Player> optTargetPlayer) {
        if (optTargetPlayer.isPresent()) { //picking card from target player's hand
            this.getPlayer().switchHandWith(optTargetPlayer.get());
        }else{ //picking card from deck
            int numberOfCardsToPick = this.getPlayer().getHand().size();
            for (int i = 0; i < numberOfCardsToPick; i++) {
                this.getPlayer().discardACard(this.getPlayer().getHand().get(i));
                this.getPlayer().pickACard();
            }
        }
    }

    @Override
    public void useEffect() {
        // No action
    }
}
