package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerAndMessageDTO(Player player, String message) implements DTO {
}
