package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.interfaces.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;

public abstract class CommonCharacter implements Character {
        private Role role;

        public CommonCharacter(Role role) {
            this.role = role;
        }

        public abstract void useEffect();

        public abstract void goldCollectedFromDisctrictType(DistrictType target);
}
