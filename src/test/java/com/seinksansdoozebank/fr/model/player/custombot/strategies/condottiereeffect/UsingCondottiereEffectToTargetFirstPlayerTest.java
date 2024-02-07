package com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.CondottiereTarget;
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

class UsingCondottiereEffectToTargetFirstPlayerTest {

    IUsingCondottiereEffectStrategy strategy;
    Condottiere spyCondottiere;
    Player mockPlayer;
    List<Opponent> opponentList;

    @BeforeEach
    void setUp() {
        strategy = new UsingCondottiereEffectToTargetFirstPlayer();
        spyCondottiere = mock(Condottiere.class);
        mockPlayer = mock(Player.class);
        when(mockPlayer.getNbGold()).thenReturn(4);
        opponentList = new ArrayList<>();
    }

    @Test
    void applyWithNotOpponentShouldThrowExceptionAndNotUseCondottiereEffect() {
        when(mockPlayer.getOpponents()).thenReturn(opponentList);
        assertThrows(IllegalStateException.class, () -> strategy.apply(mockPlayer));
        verify(spyCondottiere, never()).useEffect(any());
    }

    @Test
    void applyWithOpponentWithBishopShouldNotUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(1);
        opponentList.add(mockOpponent1);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(new Bishop());
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNull(strategy.apply(mockPlayer));
    }

    //with a dead bishop
    @Test
    void applyWithOpponentWithDeadBishopShouldUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(4);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(null);
        Card destroyableCard = new Card(District.TAVERN);
        when(mockOpponent1.getCitadel()).thenReturn(List.of(destroyableCard));
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNotNull(strategy.apply(mockPlayer));
    }

    @Test
    void applyWithOpponentWithCitadelFullShouldNotUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(8);
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNull(strategy.apply(mockPlayer));
    }

    @Test
    void applyWithOpponentWithNoDistrictInLeadingOpponentCitadelShouldNotUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        when(mockOpponent1.getOpponentCharacter()).thenReturn(new King());
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNull(strategy.apply(mockPlayer));
    }

    @Test
    void applyWithOpponentWithTooExpensiveDistrictInLeadingOpponentCitadelShouldNotUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(2);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card tooExpensiveCard = new Card(District.LIBRARY);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(tooExpensiveCard));
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNull(strategy.apply(mockPlayer));
    }

    @Test
    void applyWithOpponentWithJustDonjonInLeadingOpponentCitadelShouldNotUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(1);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card donjon = new Card(District.DONJON);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(donjon));
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertNull(strategy.apply(mockPlayer));
    }


    @Test
    void applyWithOpponentWithCheaperDistrictInLeadingOpponentCitadelShouldUseCondottiereEffect() {
        Opponent mockOpponent1 = mock(Opponent.class);
        when(mockOpponent1.nbDistrictsInCitadel()).thenReturn(0);
        opponentList.add(mockOpponent1);
        Opponent mockOpponent2 = mock(Opponent.class);
        when(mockOpponent2.nbDistrictsInCitadel()).thenReturn(2);
        when(mockOpponent2.getOpponentCharacter()).thenReturn(new King());
        Card destroyableCard = new Card(District.TAVERN);
        when(mockOpponent2.getCitadel()).thenReturn(List.of(destroyableCard));
        opponentList.add(mockOpponent2);
        when(mockPlayer.getOpponents()).thenReturn(opponentList);

        assertEquals(strategy.apply(mockPlayer), new CondottiereTarget(mockOpponent2, destroyableCard.getDistrict()));
    }
}