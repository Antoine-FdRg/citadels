package com.seinksansdoozebank.fr.model.strategies.picking;

import com.seinksansdoozebank.fr.model.player.Player;

public class PickingAlwaysGold implements IPickingStrategy{
    @Override
    public void apply(Player player) {
        player.pickGold();
    }
}
