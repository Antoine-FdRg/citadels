package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerPickCardsDTO(Player player, int nbOfCards) implements DTO {
    @Override
    public String toString() {
        return "PlayerPickCardsDTO{" +
                "player=" + player +
                ", nbOfCards=" + nbOfCards +
                '}';
    }
}
