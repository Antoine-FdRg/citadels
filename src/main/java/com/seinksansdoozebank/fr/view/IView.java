package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

public interface IView {
    void displayPlayerInfo(Player player);

    void displayRound(int roundNumber);

    void displayPlayerPlaysDistrict(Player player, District district);

    void displayWinner(String toString, int score);

    void displayPlayerStartPlaying(Player player);
}
