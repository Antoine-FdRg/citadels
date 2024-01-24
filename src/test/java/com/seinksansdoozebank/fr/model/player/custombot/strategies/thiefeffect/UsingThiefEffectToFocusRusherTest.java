package com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UsingThiefEffectToFocusRusherTest {

    Thief spyThief;
    List<Character> characters;
    IUsingThiefEffectStrategy strategy;
    Player mockPlayer;

    @BeforeEach
    void setUp() {
        spyThief = spy(Thief.class);
        characters = new ArrayList<>(List.of(new Bishop(),
                spyThief,
                new Condottiere()));
        strategy = new UsingThiefEffectToFocusRusher();
        mockPlayer = mock(Player.class);
    }

    @Test
    void applyWithArchitectInListShouldTargetArchitect() {
        characters.add(new Merchant());
        characters.add(new King());
        characters.add(new Architect());
        when(mockPlayer.getAvailableCharacters()).thenReturn(characters);
        strategy.apply(mockPlayer, spyThief);
        verify(spyThief).useEffect(new Architect());
    }

    @Test
    void applyWithMerchantInListShouldTargetMerchant() {
        characters.add(new Merchant());
        characters.add(new King());
        when(mockPlayer.getAvailableCharacters()).thenReturn(characters);
        strategy.apply(mockPlayer, spyThief);
        verify(spyThief).useEffect(new Merchant());
    }

    @Test
    void applyWithKingInListShouldTargetKing() {
        characters.add(new King());
        when(mockPlayer.getAvailableCharacters()).thenReturn(characters);
        strategy.apply(mockPlayer, spyThief);
        verify(spyThief).useEffect(new King());
    }

    @Test
    void applyWithNoImportantCharacterInListShouldTargetRandom() {
        when(mockPlayer.getAvailableCharacters()).thenReturn(characters);
        strategy.apply(mockPlayer, spyThief);
        verify(spyThief).useEffect(any());
    }
}