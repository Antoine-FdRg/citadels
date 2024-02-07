package com.seinksansdoozebank.fr.model.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {
    Bank bank;

    @BeforeEach
    void setUp() {
        bank = new Bank();
    }

    @Test
    void pickCoinWithEnoughAvailableCoinShouldReturnNB_GOLD_TO_PICK() {
        assertEquals(Bank.NB_GOLD_TO_PICK, bank.pickXCoin());
    }

    @Test
    void pickCoinWithNotEnoughAvailableCoinShouldReturnTheNumberOfAvailableCoin() {
        bank.pickXCoin(Bank.MAX_COIN - 1);
        int nbOfAvailableCoin = bank.getNbOfAvailableCoins();
        assertEquals(nbOfAvailableCoin, bank.pickXCoin());
        assertEquals(0, bank.getNbOfAvailableCoins());
    }

    @Test
    void pickXCoinWithEnoughAvailableCoinShouldReturnNumberOfWantedCoin() {
        assertEquals(1, bank.pickXCoin(1));
        assertEquals(2, bank.pickXCoin(2));
    }

    @Test
    void pickXCoinWithNotEnoughAvailableCoinShouldReturnNumberOfAvailableCoin() {
        bank.pickXCoin(Bank.MAX_COIN - 2);
        int nbOfAvailableCoin = bank.getNbOfAvailableCoins();
        assertEquals(nbOfAvailableCoin, bank.pickXCoin(3));
        assertEquals(0, bank.getNbOfAvailableCoins());
    }

    @Test
    void retrieveCoinWithTooMuchCoinShouldThrowException() {
        assertThrows(IllegalStateException.class, () -> bank.retrieveCoin(1));
    }

    @Test
    void retrieveCoinWithCorrectNumberOfCoinShouldUpdateAvailableCoin() {
        int coinPicked = bank.pickXCoin(5);
        int nbOfAvailableCoin = bank.getNbOfAvailableCoins();
        assertDoesNotThrow(() -> bank.retrieveCoin(coinPicked));
        assertEquals(coinPicked + nbOfAvailableCoin, bank.getNbOfAvailableCoins());
    }
}