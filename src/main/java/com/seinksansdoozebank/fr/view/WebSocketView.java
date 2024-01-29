package com.seinksansdoozebank.fr.view;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebSocketView implements IView {

    private final SocketIOServer server;
    private final List<SocketIOClient> clients = new ArrayList<>();

    public WebSocketView(SocketIOServer socket) {
        this.server = socket;
        setUpClientConnections();
    }

    private void setUpClientConnections() {
        // Gestion des nouvelles connexions client
        server.addConnectListener(client -> {
            System.out.println("Client connecté: " + client.getSessionId());
            clients.add(client);
        });

        // Gestion des déconnexions client
        server.addDisconnectListener(client -> {
            System.out.println("Client déconnecté: " + client.getSessionId());
            clients.remove(client);
        });

        // Ajoutez ici d'autres gestionnaires d'événements selon les besoins
    }

    private void emitEvent(String event, Object... data) {
        for (SocketIOClient client : clients) {
            client.sendEvent(event, data);
            System.out.println("Event " + event + " sent to client " + client.getSessionId() + " with data " + Arrays.toString(data));
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void displayPlayerPlaysCard(Player player, Card card) {
        emitEvent("playerPlaysCard", player, card);
    }

    @Override
    public void displayWinner(Player winner) {
        emitEvent("displayWinner", winner);
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        emitEvent("displayPlayerStartPlaying", player);
    }

    @Override
    public void displayPlayerPickCards(Player player, int numberOfCards) {
        emitEvent("displayPlayerPickCards", player, numberOfCards);
    }

    @Override
    public void displayPlayerPicksGold(Player player, int numberOfGold) {
        emitEvent("displayPlayerPicksGold", player, numberOfGold);
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {
        emitEvent("displayPlayerChooseCharacter", player);
    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {
        emitEvent("displayPlayerRevealCharacter", player);
    }

    @Override
    public void displayPlayerUseCondottiereDistrict(Player attacker, Player defender, District district) {
        emitEvent("displayPlayerUseCondottiereDistrict", attacker, defender, district);
    }

    @Override
    public void displayPlayerScore(Player player) {
        emitEvent("displayPlayerScore", player, player.getScore());
    }

    @Override
    public void displayPlayerGetBonus(Player player, int pointsBonus, String bonusName) {
        emitEvent("displayPlayerGetBonus", player, pointsBonus, bonusName);
    }

    @Override
    public void displayPlayerUseAssassinEffect(Player player, Character target) {
        emitEvent("displayPlayerUseAssassinEffect", player, target);
    }

    // Implementez les autres méthodes de l'interface IView de la même manière

    @Override
    public void displayPlayerInfo(Player player) {
        // Affiche les informations du joueur
        // Ici, on pourrait envoyer les détails du joueur au client
        emitEvent("displayPlayerInfo", player);
    }

    @Override
    public void displayUnusedCharacterInRound(Character character) {
        emitEvent("displayUnusedCharacterInRound", character);
    }

    @Override
    public void displayGameFinished() {
        emitEvent("displayGameFinished");
    }

    @Override
    public void displayRound(int roundNumber) {
        emitEvent("displayRound", roundNumber);
    }

    @Override
    public void displayPlayerError(Player player, String message) {
        emitEvent("displayPlayerError", player, message);
    }

    @Override
    public void displayPlayerStrategy(Player player, String message) {
        emitEvent("displayPlayerStrategy", player, message);
    }

    @Override
    public void displayStolenCharacter(Character character) {
        emitEvent("displayStolenCharacter", character);
    }

    @Override
    public void displayActualNumberOfGold(Player player) {
        emitEvent("displayActualNumberOfGold", player);
    }

    @Override
    public void displayPlayerUseMagicianEffect(Player player, Opponent targetPlayer) {
        emitEvent("displayPlayerUseMagicianEffect", player, targetPlayer);
    }

    @Override
    public void displayPlayerHasGotObservatory(Player player) {
        emitEvent("displayPlayerHasGotObservatory", player);
    }

    @Override
    public void displayPlayerUseThiefEffect(Player player) {
        emitEvent("displayPlayerUseThiefEffect", player);
    }

    @Override
    public void displayPlayerDiscardCard(Player player, Card card) {
        emitEvent("displayPlayerDiscardCard", player, card);
    }

    @Override
    public void displayPlayerUseLaboratoryEffect(Player player) {
        emitEvent("displayPlayerUseLaboratoryEffect", player);
    }

    @Override
    public void displayPlayerUseManufactureEffect(Player player) {
        emitEvent("displayPlayerUseManufactureEffect", player);
    }

    @Override
    public void displayGoldCollectedFromDisctrictType(Player player, int nbGold, DistrictType districtType) {
        emitEvent("displayGoldCollectedFromDisctrictType", player, nbGold, districtType);
    }

    @Override
    public void displayGameStuck() {
        emitEvent("displayGameStuck");
    }
}