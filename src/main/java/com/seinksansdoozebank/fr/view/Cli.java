package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public class Cli implements IView {

    public void displayPlayerPlaysCard(Player player, Card card) {
        System.out.println(player + " pose un/e " + card.getDistrict().getName() + " qui lui coute " + card.getDistrict().getCost() + " pièces d'or.");
    }

    public void displayWinner(String winnerName, int score) {
        System.out.println("Le joueur " + winnerName + " gagne avec un score de " + score);
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        System.out.println(player + " commence à jouer.");
    }

    private void displayPlayerHand(Player player) {
        List<Card> hand = player.getHand();
        StringBuilder sb = new StringBuilder();
        if(!hand.isEmpty()){
            if(player.getHand().size() == 1){
                sb.append("\t- la carte suivante dans sa main : \n");
            }
            else{
                sb.append("\t- les cartes suivantes dans sa main : \n");
            }
            for(int i = 0;i< hand.size();i++){
                sb.append("\t\t- ").append(hand.get(i));
                if(i!= hand.size()-1){
                    sb.append("\n");
                }
            }
        }else{
            sb.append("\t- pas de carte dans sa main.");
        }
        System.out.println(sb);
    }

    private void displayPlayerCitadel(Player player) {
        List<Card> citadel = player.getCitadel();
        StringBuilder sb = new StringBuilder();
        if(!citadel.isEmpty()){
            if(player.getCitadel().size() == 1){
                sb.append("\t- le quartier suivant dans sa citadelle : \n");
            }
            else{
                sb.append("\t- les quartiers suivants dans sa citadelle : \n");
            }
            for(int i = 0;i< citadel.size();i++){
                sb.append("\t\t- ").append(citadel.get(i));
                if(i!= citadel.size()-1){
                    sb.append("\n");
                }
            }
        }else{
            sb.append("\t- pas de quartier dans sa citadelle.");
        }
        System.out.println(sb);
    }

    @Override
    public void displayPlayerInfo(Player player) {
        System.out.println(player + " possède : \n\t- " + player.getNbGold() + " pièces d'or");
        this.displayPlayerHand(player);
        this.displayPlayerCitadel(player);
    }

    public void displayRound(int roundNumber) {
        System.out.println("########## Début du round " + roundNumber +" ##########");
    }
}
