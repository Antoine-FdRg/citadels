package com.seinksansdoozebank.fr.model.player.custombot.strategies.warlordeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Warlord;
import com.seinksansdoozebank.fr.model.character.commoncharacters.WarlordTarget;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsingWarlordEffectToTargetFirstPlayerTest {

    IUsingWarlordEffectStrategy strategy;
    Warlord spyWarlord;
    Player mockPlayer;
    List<Opponent> opponentList;

    @BeforeEach
    void setUp() {
        strategy = new UsingWarlordEffectToTargetFirstPlayer();
        spyWarlord = mock(Warlord.class);
        mockPlayer = mock(Player.class);
        when(mockPlayer.getNbGold()).thenReturn(4);
        opponentList = new ArrayList<>();
    }

    @Test
    void applyWithNotOpponentShouldThrowExceptionAndNotUseWarlordEffect() {
        assertThrows(IllegalStateException.class, () -> strategy.apply(mockPlayer, opponentList));
        verify(spyWarlord, never()).useEffect(any());
    }

    @Test
    void applyWithOpponentWithBishopShouldNotUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(1);
        opponentList.add(mockOpponent1);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(new Bishop());
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        assertNull(strategy.apply(mockPlayer, opponentList));
    }

    //with a dead bishop
    @Test
    void applyWithOpponentWithDeadBishopShouldUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(4);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(null);
        Card destroyableCard = new Card(District.TAVERN);
        when(mockOpponent1.getCitadel()).thenReturn(List.of(destroyableCard));
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        assertNotNull(strategy.apply(mockPlayer, opponentList));
    }

    @Test
    void applyWithOpponentWithCitadelFullShouldNotUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(8);
        opponentList.add(mockOpponent2);
        assertNull(strategy.apply(mockPlayer, opponentList));
    }

    @Test
    void applyWithOpponentWithNoDistrictInLeadingOpponentCitadelShouldNotUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(new King());
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        assertNull(strategy.apply(mockPlayer, opponentList));
    }

    @Test
    void applyWithOpponentWithTooExpensiveDistrictInLeadingOpponentCitadelShouldNotUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(2);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card tooExpensiveCard = new Card(District.LIBRARY);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(tooExpensiveCard));
        opponentList.add(mockOpponent2);
        assertNull(strategy.apply(mockPlayer, opponentList));
    }

    @Test
    void applyWithOpponentWithJustDonjonInLeadingOpponentCitadelShouldNotUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(1);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card donjon = new Card(District.DONJON);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(donjon));
        opponentList.add(mockOpponent2);
        assertNull(strategy.apply(mockPlayer, opponentList));
    }


    @Test
    void applyWithOpponentWithCheaperDistrictInLeadingOpponentCitadelShouldUseWarlordEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(2);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card destroyableCard = new Card(District.TAVERN);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(destroyableCard));
        opponentList.add(mockOpponent2);
        assertEquals(strategy.apply(mockPlayer, opponentList), new WarlordTarget(mockOpponent2, destroyableCard.getDistrict()));
    }
}