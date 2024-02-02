package com.seinksansdoozebank.fr.view.socket.dto;

import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Player;

public record MurdererEffectDTO(Player player, Character target) implements DTO {

    @Override
    public String toString() {
        return "MurdererEffectDTO{" +
                "player=" + player +
                ", character=" + target +
                '}';
    }
}
