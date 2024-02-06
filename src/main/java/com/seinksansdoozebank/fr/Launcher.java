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

import static com.seinksansdoozebank.fr.statistics.GameStatisticsAnalyzer.CsvCategory.BEST_AGAINST_SECOND;
import static com.seinksansdoozebank.fr.statistics.GameStatisticsAnalyzer.CsvCategory.BEST_BOTS_AGAINST;

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

        if (cmdArgs.isDemo() && !cmdArgs.isCsv()) {
            launcher.runDemo(false);
        } else if (cmdArgs.isDemo() && cmdArgs.isCsv()) {
            launcher.runDemo(true);
        } else if (cmdArgs.is2Thousands() && !cmdArgs.isCsv()) {
            launcher.twoThousand(false);
        } else if (cmdArgs.is2Thousands() && cmdArgs.isCsv()) {
            launcher.twoThousand(true);
        } else if (cmdArgs.isCsv()) {
            launcher.runDemo(true);
        }
    }


    public void runDemo(boolean saveInCsv) {
        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(saveInCsv);
        analyzer.runDemo();
    }

    public void twoThousand(boolean saveInCsv) {
        GameStatisticsAnalyzer analyzer = new GameStatisticsAnalyzer(5, saveInCsv, BEST_AGAINST_SECOND);
        analyzer.runAndAnalyze(0, 3, 3);
        analyzer = new GameStatisticsAnalyzer(5, saveInCsv, BEST_BOTS_AGAINST);
        analyzer.runAndAnalyze(0, 6, 0);
    }
}
