package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Factory for the game exposing static methods to preset create games
 */
public class GameFactory {
    private static final String NUMBER_OF_PLAYER_BETWEEN = "The number of players must be between " + Game.NB_PLAYER_MIN + " and " + Game.NB_PLAYER_MAX;

    private GameFactory() {
    }

    /**
     * Create a game with the given number of random bots
     *
     * @param view      the view to use
     * @param nbPlayers the number of random bots to add to the game
     * @return the game created
     */
    public static Game createGameOfRandomBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }

        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addRandomBot();
        }
        return gameBuilder.build();
    }

    public static Game createGameOfSmartBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addSmartBot();
        }
        return gameBuilder.build();
    }

    public static Game createGameOfCustomBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addCustomBot(
                    null,
                    new ChoosingCharacterToTargetFirstPlayer(),
                    new UsingThiefEffectToFocusRusher(),
                    new UsingMurdererEffectToFocusRusher(),
                    new UsingCondottiereEffectToTargetFirstPlayer(),
                    new CardChoosingStrategy()
            );
        }
        return gameBuilder.build();
    }

    public static Game createGameOfRichardBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addRichardBot();
        }
        return gameBuilder.build();
    }

    public static Game createGameOfAllTypeOfBot(IView view, Bank bank) {
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        gameBuilder.addRandomBot();
        gameBuilder.addSmartBot();
        gameBuilder.addCustomBot(
                null,
                new ChoosingCharacterToTargetFirstPlayer(),
                new UsingThiefEffectToFocusRusher(),
                new UsingMurdererEffectToFocusRusher(),
                new UsingCondottiereEffectToTargetFirstPlayer(),
                new CardChoosingStrategy()
        );
        gameBuilder.addRichardBot();
        gameBuilder.addBuilderBot();
        gameBuilder.addOpportunistBot();
        return gameBuilder.build();
    }

    /**
     * Creates a new game instance based on the specified number of random bots, smart bots, and custom bots.
     * This method initializes a game builder with a command-line interface and a deck, then adds the desired number
     * of random bots, smart bots, and custom bots to the game builder. Finally, it builds and returns the constructed game.
     *
     * @param numRandomBots The number of random bots to be added to the game.
     * @param numSmartBots  The number of smart bots to be added to the game.
     * @param numCustomBots The number of custom bots to be added to the game.
     * @return The newly created game instance.
     */
    public static Game createGame(int numRandomBots, int numSmartBots, int numCustomBots, int numRichardBots, int numBuilderBots, int numOpportunistBots) {
        GameBuilder gameBuilder = new GameBuilder(new Cli(), new Deck(), new Bank());
        for (int i = 0; i < numRandomBots; i++) {
            gameBuilder.addRandomBot();
        }
        for (int i = 0; i < numSmartBots; i++) {
            gameBuilder.addSmartBot();
        }
        for (int i = 0; i < numCustomBots; i++) {
            generateARandomCustomBot(gameBuilder);
        }
        for (int i = 0; i < numRichardBots; i++) {
            gameBuilder.addRichardBot();
        }
        for (int i = 0; i < numBuilderBots; i++) {
            gameBuilder.addBuilderBot();
        }
        for (int i = 0; i < numOpportunistBots; i++) {
            gameBuilder.addOpportunistBot();
        }
        return gameBuilder.build();
    }

    /**
     * Generates a random custom bot for the game using various strategies.
     * This method adds a custom bot to the game being built by the provided {@code gameBuilder}.
     * The bot is configured with different strategies for character choosing, using thief effect,
     * using murderer effect, using condottiere effect, and card choosing.
     *
     * @param gameBuilder The builder object for the game to which the custom bot will be added.
     */
    private static void generateARandomCustomBot(GameBuilder gameBuilder) {
        ICharacterChoosingStrategy choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        IUsingThiefEffectStrategy usingThiefEffectToFocusRusher = new UsingThiefEffectToFocusRusher();
        IUsingMurdererEffectStrategy usingMurdererEffectToFocusRusher = new UsingMurdererEffectToFocusRusher();
        IUsingCondottiereEffectStrategy usingCondottiereEffectToTargetFirstPlayer = new UsingCondottiereEffectToTargetFirstPlayer();
        ICardChoosingStrategy cardChoosingStrategy = new CardChoosingStrategy();
        gameBuilder.addCustomBot(null,
                choosingCharacterToTargetFirstPlayer,
                usingThiefEffectToFocusRusher,
                usingMurdererEffectToFocusRusher,
                usingCondottiereEffectToTargetFirstPlayer,
                cardChoosingStrategy);
    }
}
