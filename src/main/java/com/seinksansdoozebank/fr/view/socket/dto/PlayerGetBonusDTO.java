package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.player.Player;

public record PlayerGetBonusDTO(Player player, int bonusPoints, String bonusName) implements DTO {

    @Override
    public String toString() {
        return "PlayerGetBonusDTO{" +
                "player=" + player +
                ", pointsBonus=" + bonusPoints +
                ", bonusName='" + bonusName + '\'' +
                '}';
    }
}
