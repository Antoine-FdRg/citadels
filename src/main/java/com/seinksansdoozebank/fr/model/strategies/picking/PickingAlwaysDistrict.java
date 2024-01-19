package com.seinksansdoozebank.fr.model.strategies.picking;

import com.seinksansdoozebank.fr.model.player.Player;

public class PickingAlwaysDistrict implements IPickingStrategy{
    @Override
    public void apply(Player player) {
        player.pickTwoCardKeepOneDiscardOne();
    }
}
