package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerCollectGoldDTO(Player player, int nbOfGold, DistrictType districtType) implements DTO {
}
