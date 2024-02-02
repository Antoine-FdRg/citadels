package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerPickGoldDTO(Player player, int numberOfGold) implements DTO {

    @Override
    public String toString() {
        return "PlayerPickGoldDTO{" +
                "player=" + player +
                ", numberOfGold=" + numberOfGold +
                '}';
    }
}
