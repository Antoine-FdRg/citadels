package com.seinksansdoozebank.fr.controller;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.BuilderBot;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.OpportunistBot;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.model.player.RichardBot;
import com.seinksansdoozebank.fr.model.player.SmartBot;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBotBuilder;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.cardchoosing.ICardChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ICharacterChoosingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.IUsingCondottiereEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.IUsingMurdererEffectStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.picking.IPickingStrategy;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.IUsingThiefEffectStrategy;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder for the game
 */
public class GameBuilder {
    private static final int PLAYER_NB_GOLD_INIT = 2;
    private final IView view;
    private final Deck deck;
    private final Bank bank;
    private final List<Player> playerList;

    public GameBuilder(IView view, Deck deck, Bank bank) {
        playerList = new ArrayList<>();
        this.view = view;
        this.deck = deck;
        this.bank = bank;
    }

    int getPlayerListSize() {
        return playerList.size();
    }

    /**
     * Check if the number of players is not too high, if it is, throw an exception
     */
    void checkNbPlayers() {
        if (getPlayerListSize() > Game.NB_PLAYER_MAX) {
            throw new IllegalStateException("You can't add more than " + Game.NB_PLAYER_MAX + " players to the game");
        }
    }

    /**
     * Add a smart bot to the game
     *
     * @return the builder
     */
    public GameBuilder addSmartBot() {
        checkNbPlayers();
        playerList.add(new SmartBot(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.deck, this.view, this.bank));
        return this;
    }

    /**
     * Add a random bot to the game
     *
     * @return the builder
     */
    public GameBuilder addRandomBot() {
        checkNbPlayers();
        playerList.add(new RandomBot(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.deck, this.view, this.bank));
        return this;
    }

    public GameBuilder addCustomBot(IPickingStrategy pickingStrategy,
                                    ICharacterChoosingStrategy characterChoosingStrategy,
                                    IUsingThiefEffectStrategy thiefEffectStrategy,
                                    IUsingMurdererEffectStrategy murdererEffectStrategy,
                                    IUsingCondottiereEffectStrategy condottiereEffectStrategy,
                                    ICardChoosingStrategy cardChosingStrategy) {
        checkNbPlayers();
        playerList.add(new CustomBotBuilder(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.view, this.deck, this.bank)
                .setPickingStrategy(pickingStrategy)
                .setCharacterChoosingStrategy(characterChoosingStrategy)
                .setUsingThiefEffectStrategy(thiefEffectStrategy)
                .setUsingMurdererEffectStrategy(murdererEffectStrategy)
                .setUsingCondottiereEffectStrategy(condottiereEffectStrategy)
                .setCardChoosingStrategy(cardChosingStrategy)
                .build());
        return this;
    }

    public GameBuilder addRichardBot() {
        checkNbPlayers();
        playerList.add(new RichardBot(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.deck, this.view, this.bank));
        return this;
    }

    public GameBuilder addBuilderBot() {
        checkNbPlayers();
        playerList.add(new BuilderBot(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.deck, this.view, this.bank));
        return this;
    }

    public GameBuilder addOpportunistBot() {
        checkNbPlayers();
        playerList.add(new OpportunistBot(this.bank.pickXCoin(PLAYER_NB_GOLD_INIT), this.deck, this.view, this.bank));
        return this;
    }

    /**
     * Build the game from the arguments given to the builder
     *
     * @return the game built
     */
    public Game build() {
        if (playerList.size() < Game.NB_PLAYER_MIN) {
            throw new IllegalStateException("The number of players must be between " + Game.NB_PLAYER_MIN + " and " + Game.NB_PLAYER_MAX);
        }
        Collections.shuffle(playerList);
        for (Player player : playerList) {
            List<Opponent> opponents = new ArrayList<>(playerList);
            opponents.remove(player);
            Collections.shuffle(opponents);
            player.setOpponents(opponents);
        }
        return new Game(this.view, this.deck, this.bank, this.playerList);
    }
}
