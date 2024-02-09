package com.seinksansdoozebank.fr.view;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.logger.CustomLogger;

import java.util.List;
import java.util.logging.Level;


public class Cli implements IView {

    static final String ANSI_DEFAULT_STYLE_START = "\u001B[38;5;232m\u001B[48;5;255m";
    static final String ANSI_DEFAULT_STYLE_END = "\u001B[0m";

    @Override
    public void displayPlayerPlaysCard(Player player, Card card) {
        District builtDistrict = card.getDistrict();
        CustomLogger.log(Level.INFO, "{0} pose un/e {1} qui lui coute {2}, il lui reste {3} pièces d''or.", new Object[]{player, builtDistrict.getName(), builtDistrict.getCost(), player.getNbGold()}, player);
    }

    @Override
    public void displayWinner(Player winner) {
        CustomLogger.log(Level.INFO, "\n{0} gagne avec un score de {1}.", new Object[]{winner, winner.getScore()}, winner);
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        CustomLogger.log(Level.FINE, "\n{0} commence son tour.", player);
    }

    @Override
    public void displayPlayerPickCards(Player player, int numberOfCards) {
        CustomLogger.log(Level.INFO, "{0} pioche {1} quartier(s).", new Object[]{player, numberOfCards}, player);
    }

    @Override
    public void displayPlayerPicksGold(Player player, int numberOfGold) {
        CustomLogger.log(Level.INFO, "{0} pioche {1} pièces d''or.", new Object[]{player, numberOfGold}, player);
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {
        CustomLogger.log(Level.INFO, "{0} choisit un personnage.", player);
    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {
        CustomLogger.log(Level.INFO, "{0} se révèle être {1}.", new Object[]{player, player.getCharacter()}, player);
    }

    @Override
    public void displayPlayerUseWarlordDistrict(Player attacker, Player defender, District district) {
        CustomLogger.log(Level.INFO, "{0} utilise l''effet du Warlord pour détruire le quartier {1} de {2} en payant {3} pièces d''or.", new Object[]{attacker, district.getName(), defender, district.getCost() - 1}, attacker);
    }

    @Override
    public void displayPlayerScore(Player player) {
        CustomLogger.log(Level.INFO, "{0} fini la partie avec un score de {1}.", new Object[]{player, player.getScore()}, player);
    }

    @Override
    public void displayPlayerGetBonus(Player player, int pointsBonus, String bonusName) {
        CustomLogger.log(Level.INFO, "{0} gagne {1} points bonus pour la raison {2}.", new Object[]{player, pointsBonus, bonusName}, player);
    }

    @Override
    public void displayPlayerUseAssassinEffect(Player player, Character target) {
        CustomLogger.log(Level.INFO, "{0} utilise l''assassin pour tuer le {1}.", new Object[]{player, target}, player);
    }

    void displayPlayerHand(Player player) {
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

    void displayPlayerCitadel(Player player) {
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
        CustomLogger.log(Level.INFO, "{0} possède : \n\t- {1} pièces d''or.", new Object[]{player, player.getNbGold()}, player);
        this.displayPlayerHand(player);
        this.displayPlayerCitadel(player);
    }

    @Override
    public void displayUnusedCharacterInRound(Character character) {
        CustomLogger.log(Level.INFO, ANSI_DEFAULT_STYLE_START + "Le personnage {0} a été écarté pour cette manche." + ANSI_DEFAULT_STYLE_END, character);
    }

    @Override
    public void displayRound(int roundNumber) {
        CustomLogger.log(Level.INFO, "\n\n" + ANSI_DEFAULT_STYLE_START + "########## Début du round {0} ##########" + ANSI_DEFAULT_STYLE_END, roundNumber);
    }

    @Override
    public void displayPlayerError(Player player, String message) {
        CustomLogger.log(Level.INFO, "{0} : {1}", new Object[]{player, message}, player);
    }

    @Override
    public void displayPlayerStrategy(Player player, String message) {
        CustomLogger.log(Level.FINE, "{0} : {1}", new Object[]{player, message}, player);
    }

    @Override
    public void displayStolenCharacter(Character character) {
        CustomLogger.log(Level.INFO, "Le {0} a été volé et perd {1} pièces d''or ", new Object[]{character, character.getPlayer().getNbGold()}, character.getPlayer());
    }

    @Override
    public void displayActualNumberOfGold(Player player) {
        CustomLogger.log(Level.INFO, "Le {0} a maintenant {1} pièces d''or.", new Object[]{player.getCharacter(), player.getNbGold()}, player);
    }

    @Override
    public void displayPlayerUseMagicianEffect(Player player, Opponent targetPlayer) {
        if (targetPlayer == null) {
            CustomLogger.log(Level.INFO, "{0} utilise le magicien pour échanger sa main avec le deck.", player);
            return;
        }
        CustomLogger.log(Level.INFO, "{0} utilise le magicien pour échanger sa main avec celle du {1}.", new Object[]{player, targetPlayer}, player);
    }

    @Override
    public void displayPlayerHasGotObservatory(Player player) {
        CustomLogger.log(Level.INFO, "Le {0} possède le district Observatoire il peut donc choisir parmi 3 cartes celle qui garde dans sa main.", player);
    }

    @Override
    public void displayPlayerUseThiefEffect(Player player) {
        CustomLogger.log(Level.INFO, "{0} vient de choisir le personnage qu''il volera.", player);
    }

    @Override
    public void displayPlayerDiscardCard(Player player, Card card) {
        CustomLogger.log(Level.INFO, "{0} défausse {1}.", new Object[]{player, card}, player);
    }

    @Override
    public void displayPlayerUseLaboratoryEffect(Player player) {
        CustomLogger.log(Level.INFO, "{0} utilise le laboratoire pour défausser une carte et gagner une pièce d''or (s''il en reste dans la banque).", player);
    }

    @Override
    public void displayPlayerUseManufactureEffect(Player player) {
        CustomLogger.log(Level.INFO, "{0} utilise la manufacture pour piocher 3 cartes (en perdant 3 pièces).", player);
    }

    @Override
    public void displayGoldCollectedFromDistrictType(Player player, int nbGold, DistrictType districtType) {
        CustomLogger.log(Level.INFO, "{0} gagne {1} pièces d''or grâce à ses quartiers de type {2} et l''effet du {3}.", new Object[]{player, nbGold, districtType, player.getCharacter()}, player);
    }

    @Override
    public void displayGoldCollectedFromMerchant(Player player) {
        CustomLogger.log(Level.INFO, "{0} pioche 1 pièce d''or en début de tour car c'est un marchand.", player);
    }

    @Override
    public void displayGameFinished() {
        CustomLogger.log(Level.INFO, "\n\n" + ANSI_DEFAULT_STYLE_START + "### La partie est terminée ! ###" + ANSI_DEFAULT_STYLE_END);
    }

    @Override
    public void displayPlayerKeepBothCardsBecauseOfLibrary(Player player){
        CustomLogger.log(Level.INFO,"{0} pioche deux cartes et garde les deux car il possède la bibliothèque.", player);
    }


    @Override
    public void displayGameStuck() {
        CustomLogger.log(Level.INFO, ANSI_DEFAULT_STYLE_START + "### La partie semble bloquée, le calcul des points et des bonus va quand même être fait ###" + ANSI_DEFAULT_STYLE_END);
    }

    @Override
    public void displayPlayerUseCemeteryEffect(Player player, Card card) {
        CustomLogger.log(Level.INFO, "{0} utilise l''effet du cimetière pour récupérer {1}. Il lui reste {2} gold(s).", new Object[]{player, card, player.getNbGold()}, player);
    }
}