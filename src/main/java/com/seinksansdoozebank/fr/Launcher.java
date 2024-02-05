package com.seinksansdoozebank.fr;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;

public class Launcher {
    public static void main(String[] args) {
        // Define a class to hold your command-line parameters
        CommandLineArgs cmdArgs = new CommandLineArgs();

        // Parse command-line arguments
        JCommander.newBuilder()
                .addObject(cmdArgs)
                .build()
                .parse(args);

        Game game = new GameBuilder(new Cli(), new Deck())
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

    // Class to hold command-line parameters
    private static class CommandLineArgs {
        @Parameter(names = "--2thousands", description = "Enable 2thousands option")
        private boolean is2Thousands;

        @Parameter(names = "--demo", description = "Enable demo option")
        private boolean isDemo;

        @Parameter(names = "--csv", description = "Enable CSV option")
        private boolean isCsv;

        // Getter methods if needed
        public boolean is2Thousands() {
            return is2Thousands;
        }

        public boolean isDemo() {
            return isDemo;
        }

        public boolean isCsv() {
            return isCsv;
        }
    }
}
