package com.seinksansdoozebank.fr;

import com.beust.jcommander.JCommander;
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
        Launcher launcher = new Launcher();
        CommandLineArgs cmdArgs = new CommandLineArgs();

        // Parse command-line arguments
        JCommander.newBuilder()
                .addObject(cmdArgs)
                .build()
                .parse(args);

        if (cmdArgs.isDemo()) {
            launcher.runDemo();
        }
    }


    public void runDemo() {
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

    }

    public void csv() {

    }
}
