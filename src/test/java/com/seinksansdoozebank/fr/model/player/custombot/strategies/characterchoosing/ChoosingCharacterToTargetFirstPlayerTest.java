package com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.custombot.CustomBot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChoosingCharacterToTargetFirstPlayerTest {
    ChoosingCharacterToTargetFirstPlayer choosingCharacterToTargetFirstPlayer;
    CustomBot mockCustomBot;

    List<Character> characterList;
    Opponent leadingOpponent;

    @BeforeEach
    void setUp() {
        mockCustomBot = mock(CustomBot.class);
        choosingCharacterToTargetFirstPlayer = new ChoosingCharacterToTargetFirstPlayer();
        characterList = new ArrayList<>();
        leadingOpponent = mock(Opponent.class);

    }

    @Test
    void applyWithCondottiereInListSgouldReturnCondottiere() {
        characterList.add(new Assassin());
        characterList.add(new Magician());
        characterList.add(new Condottiere());
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));

        assertEquals(new Condottiere(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingManyGoldAndThiefInListShouldReturnThief() {
        when(leadingOpponent.getNbGold()).thenReturn(4);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Thief());
        characterList.add(new Merchant());

        assertEquals(new Thief(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingManyGoldAndNoThiefInListShouldReturnArchitect() {
        when(leadingOpponent.getNbGold()).thenReturn(4);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Merchant());
        characterList.add(new Architect());

        assertEquals(new Architect(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingFewGoldAndAssassinInListShouldReturnAssassin() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Assassin());
        characterList.add(new Merchant());
        characterList.add(new Architect());

        assertEquals(new Assassin(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithLeadingOpponentHavingFewGoldAndNoAssassinInListShouldReturnMerchant() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Thief());
        characterList.add(new Merchant());
        characterList.add(new King());

        assertEquals(new Merchant(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithNoCharacterBetterThanKingShouldReturnKing() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Bishop());
        characterList.add(new Magician());
        characterList.add(new King());

        assertEquals(new King(), choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList));
    }

    @Test
    void applyWithNoCharacterBetterThanKingAndNoKingShouldReturnRandomCharacter() {
        when(leadingOpponent.getNbGold()).thenReturn(1);
        when(mockCustomBot.getOpponents()).thenReturn(List.of(leadingOpponent));
        characterList.add(new Bishop());
        characterList.add(new Magician());

        assertTrue(characterList.contains(choosingCharacterToTargetFirstPlayer.apply(mockCustomBot, characterList)));
    }

}