package com.seinksansdoozebank.fr;

import com.beust.jcommander.JCommander;
import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.statistics.GameStatisticsAnalyzer;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;
import com.seinksansdoozebank.fr.view.logger.CustomStatisticsLogger;

import java.util.logging.Level;

public class Launcher {
    public static void main(String[] args) {
        // Define a class to hold your command-line parameters
        Launcher launcher = new Launcher();
        CommandLineArgs cmdArgs = new CommandLineArgs();

        // Parse command-line arguments
        JCommander.newBuilder()
                .addObject(cmdArgs)
                .build()
                .parse(args);

        if (cmdArgs.isDemo()) {
            launcher.runDemo();
        } else if (cmdArgs.is2Thousands()) {
            launcher.twoThousand();
        } else if (cmdArgs.isCsv()) {
            launcher.csv();
        }
    }


    public void runDemo() {
        CustomLogger.setLevel(Level.ALL);
        Bank.reset();
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

    public void twoThousand() {
        CustomStatisticsLogger.setLevel(Level.INFO);
        CustomLogger.setLevel(Level.OFF);
        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(10000);
        analyzer.runAndAnalyze(2, 2, 2);
    }

    public void csv() {

    }
}
