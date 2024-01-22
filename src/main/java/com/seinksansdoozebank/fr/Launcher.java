package com.seinksansdoozebank.fr;

import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.PickingAlwaysGold;
import com.seinksansdoozebank.fr.view.Cli;

public class Launcher {
    public static void main(String[] args) {
        Game game = new GameBuilder(new Cli(), new Deck())
                .addRandomBot()
                .addSmartBot()
                .addRandomBot()
                .addCustomBot(new PickingAlwaysGold(), new ChoosingCharacterToTargetFirstPlayer())
                .build();
        game.run();
    }
}
