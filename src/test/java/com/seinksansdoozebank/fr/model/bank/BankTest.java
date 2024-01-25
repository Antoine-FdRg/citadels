package com.seinksansdoozebank.fr.model.bank;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BankTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getInstanceIsNotNull() {
        assertNotNull(Bank.getInstance());
    }

    @Test
    void resetCreateANewInstance() {
        Bank initialBank = Bank.getInstance();
        Bank.reset();
        Bank newBank = Bank.getInstance();
        assertNotEquals(initialBank, newBank);
        assertEquals(Bank.MAX_COIN, Bank.getInstance().getNbOfAvailableCoin());
    }

    @Test
    void pickCoinWithEnoughAvailableCoinShouldReturnNB_GOLD_TO_PICK() {
        Bank.reset();
        assertEquals(Bank.NB_GOLD_TO_PICK, Bank.getInstance().pickXCoin());
    }

    @Test
    void pickCoinWithNotEnoughAvailableCoinShouldReturnTheNumberOfAvailableCoin() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN - 1);
        int nbOfAvailableCoin = Bank.getInstance().getNbOfAvailableCoin();
        assertEquals(nbOfAvailableCoin, Bank.getInstance().pickXCoin());
        assertEquals(0, Bank.getInstance().getNbOfAvailableCoin());
    }

    @Test
    void pickXCoinWithEnoughAvailableCoinShouldReturnNumberOfWantedCoin() {
        Bank.reset();
        assertEquals(1, Bank.getInstance().pickXCoin(1));
        assertEquals(2, Bank.getInstance().pickXCoin(2));
    }

    @Test
    void pickXCoinWithNotEnoughAvailableCoinShouldReturnNumberOfAvailableCoin() {
        Bank.reset();
        Bank.getInstance().pickXCoin(Bank.MAX_COIN - 1);
        int nbOfAvailableCoin = Bank.getInstance().getNbOfAvailableCoin();
        assertEquals(nbOfAvailableCoin, Bank.getInstance().pickXCoin(2));
        assertEquals(0, Bank.getInstance().getNbOfAvailableCoin());
    }

    @Test
    void retrieveCoinWithTooMuchCoinShouldThrowException() {
        Bank.reset();
        assertThrows(IllegalStateException.class, () -> Bank.getInstance().retrieveCoin(1));
    }

    @Test
    void retrieveCoinWithCorrectNumberOfCoinShouldUpdateAvailableCoin() {
        Bank.reset();
        int coinPicked = Bank.getInstance().pickXCoin(5);
        int nbOfAvailableCoin = Bank.getInstance().getNbOfAvailableCoin();
        assertDoesNotThrow(() -> Bank.getInstance().retrieveCoin(coinPicked));
        assertEquals(coinPicked + nbOfAvailableCoin, Bank.getInstance().getNbOfAvailableCoin());
    }
}