package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.CardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
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

    public static Game createGameOfBuilderBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addBuilderBot();
        }
        return gameBuilder.build();
    }

    public static Game createGameOfOpportunistBot(IView view, Bank bank, int nbPlayers) {
        if (nbPlayers < Game.NB_PLAYER_MIN || nbPlayers > Game.NB_PLAYER_MAX) {
            throw new IllegalArgumentException(NUMBER_OF_PLAYER_BETWEEN);
        }
        GameBuilder gameBuilder = new GameBuilder(view, new Deck(), bank);
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addOpportunistBot();
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
}
