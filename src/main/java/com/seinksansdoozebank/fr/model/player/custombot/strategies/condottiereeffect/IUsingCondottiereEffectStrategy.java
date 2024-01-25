package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.player.Player;

public interface IUsingCondottiereEffectStrategy {
    void apply(Player player, Condottiere condottiere);
}
