package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.view.IView;

/**
 * Factory for the game exposing static methods to preset create games
 */
public class GameFactory {
    private GameFactory() {
    }

    /**
     * Create a game with the given number of random bots
     * @param view the view to use
     * @param nbPlayers the number of random bots to add to the game
     * @return the game created
     */
    public static Game createGameOfRandomBot(IView view, int nbPlayers) {
        GameBuilder gameBuilder = new GameBuilder(view, new Deck());
        for (int i = 0; i < nbPlayers; i++) {
            gameBuilder.addRandomBot();
        }
        return gameBuilder.build();
    }
}
