package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;

public class Cli implements IView {

    public void displayPlayerPlaysDistrict(Player player, District district) {
        System.out.println(player + " pose un/e " + district.getName() + " qui lui coute " + district.getCost() + " il lui reste " + player.getNbGold() + " pièces d'or");
    }

    public void displayWinner(String winnerName, int score) {
        System.out.println("Le joueur " + winnerName + " gagne avec un score de " + score);
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        System.out.println(player + " commence à jouer.");
    }

    @Override
    public void displayPlayerHand(Player player, List<District> hand) {
        StringBuilder sb = new StringBuilder();
        if(!hand.isEmpty()){
            sb.append(player).append(" possède les cartes suivante dans sa main : \n");
            for(int i = 0;i< hand.size();i++){
                sb.append("\t- ").append(hand.get(i));
                if(i!= hand.size()-1){
                    sb.append("\n");
                }
            }
        }else{
            sb.append(player).append(" n'a pas de carte dans sa main.");
        }
        System.out.println(sb);
    }

    @Override
    public void displayPlayerCitadel(Player player, List<District> citadel) {
        StringBuilder sb = new StringBuilder();
        if(!citadel.isEmpty()){
            sb.append(player).append(" possède les quartiers suivants dans sa citadelle : \n");
            for(int i = 0;i< citadel.size();i++){
                sb.append("\t- ").append(citadel.get(i));
                if(i!= citadel.size()-1){
                    sb.append("\n");
                }
            }
        }else{
            sb.append(player).append(" n'a pas de carte dans sa citadelle.");
        }
        System.out.println(sb);
    }

    public void displayRound(int round) {
        System.out.println("########## Début du round " + round+" ##########");
    }
}
