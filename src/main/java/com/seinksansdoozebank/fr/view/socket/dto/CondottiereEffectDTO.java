package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

public record CondottiereEffectDTO(Player attacker, Player target, District district) implements DTO {

    @Override
    public String toString() {
        return "CondottiereEffectDTO{" +
                "attacker=" + attacker +
                ", defender=" + target +
                ", district=" + district +
                '}';
    }
}
