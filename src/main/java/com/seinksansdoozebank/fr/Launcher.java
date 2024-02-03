package com.seinksansdoozebank.fr;

import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;

public class Launcher {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            CustomLogger.resetAvailableColors();
            Cli cli = new Cli();
            Game game = new GameBuilder(cli, new Deck())
                    .addRandomBot()
                    .addSmartBot()
                    .addRandomBot()
                    .addCustomBot(null, new ChoosingCharacterToTargetFirstPlayer(),
                            new UsingThiefEffectToFocusRusher(),
                            new UsingMurdererEffectToFocusRusher(),
                            new UsingCondottiereEffectToTargetFirstPlayer(),
                            new CardChoosingStrategy())
                    .build();
            game.run();
        }
    }
}
