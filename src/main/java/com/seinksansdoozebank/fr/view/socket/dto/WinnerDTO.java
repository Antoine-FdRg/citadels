package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record WinnerDTO(Player winner, int score) implements DTO {
    @Override
    public String toString() {
        return "WinnerDTO{" +
                "winner=" + winner +
                ", score=" + score +
                '}';
    }
}
