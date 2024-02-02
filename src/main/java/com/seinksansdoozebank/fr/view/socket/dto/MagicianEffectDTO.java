package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

public record MagicianEffectDTO(Player player, Opponent target) implements DTO {
}
