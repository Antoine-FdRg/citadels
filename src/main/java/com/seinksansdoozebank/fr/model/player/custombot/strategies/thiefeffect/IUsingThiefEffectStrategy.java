package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;

public interface IUsingThiefEffectStrategy {
    void apply(Player player, Thief thief);
}
