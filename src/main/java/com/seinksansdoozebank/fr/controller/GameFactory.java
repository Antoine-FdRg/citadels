package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.IUsingWarlordEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect.UsingWarlordEffectToTargetFirstPlayer;
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
     * @param bank      the bank to use
     * @param nbPlayers the number of random bots to add to the game
     * @return the game created
     */
    public static Game createGameOfRandomBot(IView view, Bank bank, int nbPlayers, int numberOfDistrictsNeeded) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }

        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank, numberOfDistrictsNeeded);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addRandomBot();
        }
        return gameBuilder.build();
    }

    /**
     * Create a game with the given number of smart bots
     *
     * @param view      the view to use
     * @param bank      the bank to use
     * @param nbPlayers the number of smart bots to add to the game
     * @return the game created
     */
    public static Game createGameOfSmartBot(IView view, Bank bank, int nbPlayers, int numberOfDistrictsNeeded) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank,numberOfDistrictsNeeded);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addSmartBot();
        }
        return gameBuilder.build();
    }

    /**
     * Create a game with the given number of custom bots
     * @param view the view to use
     * @param bank the bank to use
     * @param nbPlayers the number of custom bots to add to the game
     * @return the game created
     */
    public static Game createGameOfCustomBot(IView view, Bank bank, int nbPlayers, int numberOfDistrictsNeeded) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank, numberOfDistrictsNeeded);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addCustomBot(
                    null,
                    new ChoosingCharacterToTargetFirstPlayer(),
                    new UsingThiefEffectToFocusRusher(),
                    new UsingMurdererEffectToFocusRusher(),
                    new UsingWarlordEffectToTargetFirstPlayer(),
                    new CardChoosingStrategy()
            );
        }
        return gameBuilder.build();
    }

    /**
     * Create a game with the given number of Richard bots
     * @param view the view to use
     * @param bank the bank to use
     * @param nbPlayers the number of Richard bots to add to the game
     * @return the game created
     */
    public static Game createGameOfRichardBot(IView view, Bank bank, int nbPlayers, int numberOfDistrictsNeeded) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank, numberOfDistrictsNeeded);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addRichardBot();
        }
        return gameBuilder.build();
    }

    /**
     * Create a game with the given number of Builder bots
     * @param view the view to use
     * @param bank the bank to use
     * @return the game created
     */
    public static Game createGameOfAllTypeOfBot(IView view, Bank bank, int numberOfDistrictsNeeded) {
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank, numberOfDistrictsNeeded);
        gameBuilder.addRandomBot();
        gameBuilder.addSmartBot();
        gameBuilder.addCustomBot(
                null,
                new ChoosingCharacterToTargetFirstPlayer(),
                new UsingThiefEffectToFocusRusher(),
                new UsingMurdererEffectToFocusRusher(),
                new UsingWarlordEffectToTargetFirstPlayer(),
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
     * @param numRichardBots The number of Richard bots to be added to the game.
     * @param numBuilderBots The number of Builder bots to be added to the game.
     * @param numOpportunistBots The number of Opportunist bots to be added to the game.
     * @return The newly created game instance.
     */
    public static Game createCustomGame(int numRandomBots, int numSmartBots, int numCustomBots, int numRichardBots, int numBuilderBots, int numOpportunistBots, int numberOfDistrictsNeeded) {
        if (numRandomBots + numSmartBots + numCustomBots + numRichardBots + numBuilderBots + numOpportunistBots < Game.NB_PLAYER_MIN || numRandomBots + numSmartBots + numCustomBots + numRichardBots + numBuilderBots + numOpportunistBots > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(new Cli(), new Deck(), new Bank(), numberOfDistrictsNeeded);
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
     * using murderer effect, using warlord effect, and card choosing.
     *
     * @param gameBuilder The builder object for the game to which the custom bot will be added.
     */
    private static void generateARandomCustomBot(GameBuilder gameBuilder) {
        ICharacterChoosingStrategy choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        IUsingThiefEffectStrategy usingThiefEffectToFocusRusher = new UsingThiefEffectToFocusRusher();
        IUsingMurdererEffectStrategy usingMurdererEffectToFocusRusher = new UsingMurdererEffectToFocusRusher();
        IUsingWarlordEffectStrategy usingWarlordEffectToTargetFirstPlayer = new UsingWarlordEffectToTargetFirstPlayer();
        ICardChoosingStrategy cardChoosingStrategy = new CardChoosingStrategy();
        gameBuilder.addCustomBot(null,
                choosingCharacterToTargetFirstPlayer,
                usingThiefEffectToFocusRusher,
                usingMurdererEffectToFocusRusher,
                usingWarlordEffectToTargetFirstPlayer,
                cardChoosingStrategy);
    }
}
