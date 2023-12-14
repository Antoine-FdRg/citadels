package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public abstract class CommonCharacter implements Character {
        private Role role;

        /**
         * Set the player of the character
         * @param player the player to set
         */
        public abstract void setPlayer(Player player);

        /**
         * Set the citadel of the character
         * @param citadel the citadel to set
         */
        public abstract void setCitadel(List<District> citadel);

        protected CommonCharacter(Role role) {
            this.role = role;
        }

        public abstract void useEffect();

        /**
         * For each district in the citadel of the target type, the player will collect one gold
         */
        public abstract void goldCollectedFromDisctrictType();
}
