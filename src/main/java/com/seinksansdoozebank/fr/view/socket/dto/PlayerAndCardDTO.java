package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerAndCardDTO(Player player, Card card) implements DTO {

    @Override
    public String toString() {
        return "PlayerPlaysCardDTO{" +
                "player=" + player +
                ", card=" + card +
                '}';
    }
}
