package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.character.commoncharacters.CondottiereTarget;
import com.seinksansdoozebank.fr.model.player.Player;

public interface IUsingCondottiereEffectStrategy {
    CondottiereTarget apply(Player player);
}
