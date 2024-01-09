package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CharacterTest {

    private Thief thief;
    private Merchant merchant;
    private List<Player> listOfPlayer;

    private Player thiefPlayer;
    private Player merchantPlayer;
    @Mock
    private IView view;

    @BeforeEach
    void setUp() {
        //Création d'un player de type voleur
        Deck thiefDeck = new Deck();
        thiefPlayer = spy(new RandomBot(5, thiefDeck, view));
        thief = new Thief();
        when(thiefPlayer.getCharacter()).thenReturn(thief);
        thief.setPlayer(thiefPlayer);

        //Création d'un player de type marchand
        Deck merchantDeck = new Deck();
        merchantPlayer = spy(new RandomBot(3, merchantDeck, view));
        merchant =spy( new Merchant());
        when(merchantPlayer.getCharacter()).thenReturn(merchant);
        merchant.setPlayer(merchantPlayer);
        when(merchant.getSavedThief()).thenReturn(thiefPlayer);

        listOfPlayer = new ArrayList<>(List.of(merchantPlayer, thiefPlayer));
    }

    /**
     * We verify that the number of gold of the thief increases well thanks
     * to the number of golf of the character which is stolen
     */
    @Test
    void isStolenGetNumberOfGoldOfThiefTest() {
        merchant.isStolen();
        assertEquals(8, thiefPlayer.getNbGold());
    }

    /**
     * We verify that the number of gold of the marchant decreases well because all it gold
     * has been stolen by the thief
     */
    @Test
    void isStolenGetNumberOfGoldOfMerchantTest() {
        merchant.isStolen();
        assertEquals(0, merchantPlayer.getNbGold());
    }


}
