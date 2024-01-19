package com.seinksansdoozebank.fr.model.strategies.picking;

import com.seinksansdoozebank.fr.model.player.Player;

/**
 * Implementation of the picking strategy where the player always pick district
 */
public class PickingAlwaysDistrict implements IPickingStrategy{
    @Override
    public void apply(Player player) {
        player.pickTwoCardKeepOneDiscardOne();
    }
}
