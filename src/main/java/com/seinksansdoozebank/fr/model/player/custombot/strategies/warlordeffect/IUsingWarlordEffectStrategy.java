package com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect;

import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public interface IUsingWarlordEffectStrategy {
    WarlordTarget apply(Player player, List<Opponent> opponents);
}
