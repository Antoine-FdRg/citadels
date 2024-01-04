package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.Optional;

import java.util.List;

public class Cli implements IView {

    public void displayPlayerPlaysCard(Player player, Optional<Card> optionalCard) {
        if (optionalCard.isEmpty()) {
            System.out.println(player + " ne pose pas de quartier.");
        } else {
            District builtDistrict = optionalCard.get().getDistrict();
            System.out.println(player + " pose un/e " + builtDistrict.getName() + " qui lui coute " + builtDistrict.getCost() + ", il lui reste " + player.getNbGold() + " pièces d'or.");
        }
    }

    public void displayWinner(Player winner) {
        System.out.println(winner + " gagne avec un score de " + winner.getScore() + ".");
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        System.out.println(player + " commence à jouer.");
    }

    @Override
    public void displayPlayerPickCard(Player player) {
        System.out.println(player + " pioche un quartier.");
    }

    @Override
    public void displayPlayerPicksGold(Player player) {
        System.out.println(player + " pioche 2 pièces d'or.");
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {
        System.out.println(player + " choisit un personnage.");
    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {
        System.out.println(player + " se révèle être " + player.getCharacter() + ".");
    }

    @Override
    public void displayPlayerDestroyDistrict(Player attacker, Player defender, District district) {
        System.out.println(attacker + " détruit le quartier " + district.getName() + " de " + defender + " en payant " + district.getCost() + 1 + " pièces d'or.");
    }

    private void displayPlayerHand(Player player) {
        List<Card> hand = player.getHand();
        StringBuilder sb = new StringBuilder();
        if (!hand.isEmpty()) {
            if (hand.size() == 1) {
                sb.append("\t- la carte suivante dans sa main : \n");
            } else {
                sb.append("\t- les cartes suivantes dans sa main : \n");
            }
            for (int i = 0; i < hand.size(); i++) {
                sb.append("\t\t- ").append(hand.get(i));
                if (i != hand.size() - 1) {
                    sb.append("\n");
                }
            }
        } else {
            sb.append("\t- pas de carte dans sa main.");
        }
        System.out.println(sb);
    }

    private void displayPlayerCitadel(Player player) {
        List<Card> citadel = player.getCitadel();
        StringBuilder sb = new StringBuilder();
        if (!citadel.isEmpty()) {
            if (citadel.size() == 1) {
                sb.append("\t- le quartier suivant dans sa citadelle : \n");
            } else {
                sb.append("\t- les quartiers suivants dans sa citadelle : \n");
            }
            for (int i = 0; i < citadel.size(); i++) {
                sb.append("\t\t- ").append(citadel.get(i));
                if (i != citadel.size() - 1) {
                    sb.append("\n");
                }
            }
        } else {
            sb.append("\t- pas de quartier dans sa citadelle.");
        }
        System.out.println(sb);
    }

    @Override
    public void displayPlayerInfo(Player player) {
        System.out.println(player + " possède : \n\t- " + player.getNbGold() + " pièces d'or.");
        this.displayPlayerHand(player);
        this.displayPlayerCitadel(player);
    }

    public void displayRound(int roundNumber) {
        System.out.println("########## Début du round " + roundNumber + " ##########");
    }
}
