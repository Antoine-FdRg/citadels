package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Optional;

public interface IView {
    void displayPlayerInfo(Player player);

    void displayRound(int roundNumber);

    void displayPlayerPlaysDistrict(Player player, Optional<District> district);

    void displayWinner(Player winner);

    void displayPlayerStartPlaying(Player player);
    
    void displayPlayerPickDistrict(Player player);

    void displayPlayerPicksGold(Player player);
}
