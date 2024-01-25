package com.seinksansdoozebank.fr.model.character.abstracts;

import com.seinksansdoozebank.fr.model.bank.Bank;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.model.player.RandomBot;
import com.seinksansdoozebank.fr.view.IView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class CharacterTest {

    private Merchant merchant;
    private Player thiefPlayer;
    private Player merchantPlayer;
    @Mock
    private IView view;

    @BeforeEach
    void setUp() {
        Bank.reset();
        Bank.getInstance().pickCoin(Bank.MAX_COIN / 2);
        //Création d'un player de type voleur
        Deck thiefDeck = new Deck();
        thiefPlayer = spy(new RandomBot(5, thiefDeck, view));
        Thief thief = new Thief();
        when(thiefPlayer.getCharacter()).thenReturn(thief);
        thief.setPlayer(thiefPlayer);

        //Création d'un player de type marchand
        Deck merchantDeck = new Deck();
        merchantPlayer = spy(new RandomBot(3, merchantDeck, view));
        merchant =spy( new Merchant());
        when(merchantPlayer.getCharacter()).thenReturn(merchant);
        merchant.setPlayer(merchantPlayer);
        when(merchant.getSavedThief()).thenReturn(thiefPlayer);
    }

    /**
     * We verify that the number of gold of the thief increases well thanks
     * to the number of golf of the character which is stolen and that the stolen character gold is 0.
     */
    @Test
    void isStolenOnMerchantShouldSetThiefGoldTo8AndMerchantGoldTo0() {
        merchant.isStolen();
        assertEquals(8, thiefPlayer.getNbGold());
        assertEquals(0, merchantPlayer.getNbGold());
    }
 


}
