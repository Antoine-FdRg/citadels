package com.seinksansdoozebank.fr.model.bank;

/**
 * The bank is a singleton that ensure that there is only 30 coins in the game.
 */
public class Bank {
    /**
     * The maximum number of coin in the game
     */
    private static final int MAX_COIN = 30;
    /**
     * The number of coin that a player can pick in his pick phase
     */
    private static final int NB_GOLD_TO_PICK = 2;

    /**
     * The number of coin that still available to pick in the bank
     */
    private int nbOfAvailableCoin;
    /**
     * The instance of the bank
     */
    private static Bank instance = null;

    /**
     * Get the instance of the bank (singleton)
     *
     * @return the instance of the bank
     */
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public static void reset() {
        instance = null;
    }

    private Bank() {
        this.nbOfAvailableCoin = MAX_COIN;
    }

    /**
     * Pick 2 coins from the bank (useful for the pick phase)
     *
     * @return the number of coin picked (2 or less if there is not enough coin)
     */
    public int pickCoin() {
        if (nbOfAvailableCoin >= NB_GOLD_TO_PICK) {
            nbOfAvailableCoin -= NB_GOLD_TO_PICK;
            return NB_GOLD_TO_PICK;
        } else {
            int tmp = nbOfAvailableCoin;
            nbOfAvailableCoin = 0;
            return tmp;
        }
    }

    /**
     * Pick a specific number of coin from the bank
     *
     * @param nbOfCoin the number of coin to pick
     * @return the number of coin picked (nbOfCoin or less if there is not enough coin)
     */
    public int pickCoin(int nbOfCoin) {
        if (nbOfAvailableCoin >= nbOfCoin) {
            nbOfAvailableCoin -= nbOfCoin;
            return nbOfCoin;
        } else {
            int tmp = nbOfAvailableCoin;
            nbOfAvailableCoin = 0;
            return tmp;
        }
    }

    /**
     * Retrieve a specific number of coin to the bank
     *
     * @param nbOfCoin the number of coin to retrieve
     */
    public void retrieveCoin(int nbOfCoin) {
        nbOfAvailableCoin += nbOfCoin;
        if (nbOfAvailableCoin > MAX_COIN) {
            throw new IllegalStateException("The bank has more than 30 coins");
        }
    }

}
