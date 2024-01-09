package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;

import java.util.List;
import java.util.logging.Level;


public class Cli implements IView {

    @Override
    public void displayPlayerPlaysCard(Player player, List<Card> optionalCard) {
        if (optionalCard.isEmpty()) {
            CustomLogger.log(Level.INFO,"{0} ne pose pas de quartier. ",player );
        } else {
            for (Card card : optionalCard) {
                District builtDistrict = card.getDistrict();
                CustomLogger.log(Level.INFO,"{0} pose un/e {1} qui lui coute {2}, il lui reste {3}  pièces d''or.", new Object[]{player, builtDistrict.getName(), builtDistrict.getCost(), player.getNbGold()});
            }
        }
    }

    public void displayWinner(Player winner) {
        CustomLogger.log(Level.INFO, "{0} gagne avec un score de {1} .", new Object[]{winner, winner.getScore()});
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        CustomLogger.log(Level.INFO, "\n{0} commence à jouer.", player);
    }

    @Override
    public void displayPlayerPickCard(Player player) {
        CustomLogger.log(Level.INFO, "{0} pioche un quartier.", player);
    }

    @Override
    public void displayPlayerPicksGold(Player player) {
        CustomLogger.log(Level.INFO, "{0} pioche 2 pièces d'or.", player);
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {
        CustomLogger.log(Level.INFO, "{0} choisit un personnage.", player);
    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {
        CustomLogger.log(Level.INFO, "{0} se révèle être {1} .", new Object[]{player, player.getCharacter()});
    }

    @Override
    public void displayPlayerDestroyDistrict(Player attacker, Player defender, District district) {
        CustomLogger.log(Level.INFO, "{0} détruit le quartier {1} de {2} en payant {3} pièces d'or.", new Object[]{attacker, district.getName(), defender, district.getCost() + 1});
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
        CustomLogger.log(Level.INFO, sb.toString(), player);
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
        CustomLogger.log(Level.INFO, sb.toString(), player);
    }

    @Override
    public void displayPlayerInfo(Player player) {
        CustomLogger.log(Level.INFO, "{0} possède : \n\t- {1} pièces d'or.", new Object[]{player, player.getNbGold()});
        this.displayPlayerHand(player);
        this.displayPlayerCitadel(player);
    }

    public void displayRound(int roundNumber) {
        CustomLogger.log(Level.INFO, "\u001B[31m\n\n########## Début du round {0} ##########\u001B[0m", roundNumber);
    }
}
