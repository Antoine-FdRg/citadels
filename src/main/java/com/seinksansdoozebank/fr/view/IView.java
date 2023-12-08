package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public interface IView {
    void displayPlayerHand(Player player, List<District> hand);
    void displayPlayerCitadel(Player player, List<District> citadel);

    void displayRound(int i);

    void displayPlayerPlaysDistrict(Player player, District district);

    void displayWinner(String toString, int score);

    void displayPlayerStartPlaying(Player player);
}
