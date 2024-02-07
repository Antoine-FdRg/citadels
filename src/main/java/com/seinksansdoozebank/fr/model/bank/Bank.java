package com.seinksansdoozebank.fr.model.bank;

/**
 * The bank is a singleton that ensure that there is only 30 coins in the game.
 */
public class Bank {
    /**
     * The maximum number of coin in the game
     */
    public static final int MAX_COIN = 30;
    /**
     * The number of coin that a player can pick in his pick phase
     */
    static final int NB_GOLD_TO_PICK = 2;

    /**
     * The number of coin that still available to pick in the bank
     */
    private int nbOfAvailableCoins;

    public Bank() {
        this.nbOfAvailableCoins = MAX_COIN;
    }

    public int getNbOfAvailableCoins() {
        return nbOfAvailableCoins;
    }

    /**
     * Pick 2 coins from the bank (useful for the pick phase)
     *
     * @return the number of coin picked (2 or less if there is not enough coin)
     */
    public int pickXCoin() {
        if (nbOfAvailableCoins >= NB_GOLD_TO_PICK) {
            nbOfAvailableCoins -= NB_GOLD_TO_PICK;
            return NB_GOLD_TO_PICK;
        } else {
            int tmp = nbOfAvailableCoins;
            nbOfAvailableCoins = 0;
            return tmp;
        }
    }

    /**
     * Pick a specific number of coin from the bank
     *
     * @param nbOfCoin the number of coin to pick
     * @return the number of coin picked (nbOfCoin or less if there is not enough coin)
     */
    public int pickXCoin(int nbOfCoin) {
        if (nbOfAvailableCoins >= nbOfCoin) {
            nbOfAvailableCoins -= nbOfCoin;
            return nbOfCoin;
        } else {
            int tmp = nbOfAvailableCoins;
            nbOfAvailableCoins = 0;
            return tmp;
        }
    }

    /**
     * Retrieve a specific number of coin to the bank
     *
     * @param nbOfCoin the number of coin to retrieve
     */
    public void retrieveCoin(int nbOfCoin) {
        nbOfAvailableCoins += nbOfCoin;
        if (nbOfAvailableCoins > MAX_COIN) {
            throw new IllegalStateException("The bank has more than 30 coins");
        }
    }

}
