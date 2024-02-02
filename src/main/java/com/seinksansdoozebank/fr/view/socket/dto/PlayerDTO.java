package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerDTO(Player player) implements DTO {
    @Override
    public String toString() {
        return player.toString();
    }
}
