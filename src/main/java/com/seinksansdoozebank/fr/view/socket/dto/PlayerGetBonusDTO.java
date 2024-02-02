package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerGetBonusDTO(Player player, int bonusPoint, String bonusName) implements DTO {

    @Override
    public String toString() {
        return "PlayerGetBonusDTO{" +
                "player=" + player +
                ", pointsBonus=" + bonusPoint +
                ", bonusName='" + bonusName + '\'' +
                '}';
    }
}
