package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CliLogger implements  IView{

    private static final Logger logger = Logger.getLogger(CliLogger.class.getName());

    public CliLogger() {
        // Niveau de log pour afficher toutes les infos
        logger.setLevel(Level.ALL);

    }
    public void displayPlayerPlaysCard(Player player, Optional<Card> optionalCard) {
        if (optionalCard.isEmpty()) {
            String res=player+" ne pose pas de quartier.";
            logger.info(res);
        } else {
            District builtDistrict = optionalCard.get().getDistrict();
            String res=player + " pose un/e " + builtDistrict.getName() + " qui lui coute " + builtDistrict.getCost() + ", il lui reste " + player.getNbGold() + " pièces d'or";
            logger.info(res);
        }
    }

    public void displayWinner(Player winner) {
        String res=winner + " gagne avec un score de " + winner.getScore();
        logger.info(res);
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        String res= player+ " commence à jouer.";
        logger.info(res);
    }

    @Override
    public void displayPlayerPickCard(Player player) {
        String res=player + " pioche un quartier.";
        logger.info(res);
    }

    @Override
    public void displayPlayerPicksGold(Player player) {
        String res=player + " pioche 2 pièces d'or.";
        logger.info(res);
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {

    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {

    }

    private void displayPlayerHand(Player player) {
        List<Card> hand = player.getHand();
        StringBuilder sb = new StringBuilder();
        if(!hand.isEmpty()){
            if(hand.size() == 1){
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
        String res=String.valueOf(sb);
       logger.info(res);
    }

    private void displayPlayerCitadel(Player player) {
        List<Card> citadel = player.getCitadel();
        StringBuilder sb = new StringBuilder();
        if(!citadel.isEmpty()){
            if(citadel.size() == 1){
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
        String res=String.valueOf(sb);
        logger.info(res);
    }

    @Override
    public void displayPlayerInfo(Player player) {
        String res=player + " possède : \n\t- " + player.getNbGold() + " pièces d'or";
        logger.info(res);
        this.displayPlayerHand(player);
        this.displayPlayerCitadel(player);
    }

    public void displayRound(int roundNumber) {
        String res="########## Début du round " + roundNumber +" ##########";
        logger.info(res );
    }
}
