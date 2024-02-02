package com.seinksansdoozebank.fr.view.socket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.player.Opponent;
import com.seinksansdoozebank.fr.model.player.Player;
import com.seinksansdoozebank.fr.view.IView;
import com.seinksansdoozebank.fr.view.socket.dto.CharacterDTO;
import com.seinksansdoozebank.fr.view.socket.dto.CondottiereEffectDTO;
import com.seinksansdoozebank.fr.view.socket.dto.DTO;
import com.seinksansdoozebank.fr.view.socket.dto.EmptyDTO;
import com.seinksansdoozebank.fr.view.socket.dto.MagicianEffectDTO;
import com.seinksansdoozebank.fr.view.socket.dto.MurdererEffectDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerAndMessageDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerCollectGoldDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerAndCardDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerGetBonusDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerPickCardsDTO;
import com.seinksansdoozebank.fr.view.socket.dto.PlayerPickGoldDTO;
import com.seinksansdoozebank.fr.view.socket.dto.RoundNumberDTO;
import com.seinksansdoozebank.fr.view.socket.dto.WinnerDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WebSocketView implements IView {

    private final SocketIOServer server;
    public final List<SocketIOClient> clients;
    public final CountDownLatch latch;

    public WebSocketView(SocketIOServer socket, CountDownLatch latch) {
        this.server = socket;
        this.clients = new ArrayList<>();
        this.latch = latch;
        this.setUpClientsConnectionMangementEvents();
    }

    public void addClient(SocketIOClient client) {
        if (!clients.contains(client)) {
            clients.add(client);
            latch.countDown();
            System.out.println("Client connecté: " + client.getSessionId());
        }
    }

    private void setUpClientsConnectionMangementEvents() {
        server.addConnectListener(this::addClient);

        // Gestion des déconnexions client
        server.addDisconnectListener(client -> {
            System.out.println("Client déconnecté: " + client.getSessionId());
            clients.remove(client);
        });

        // Ajoutez ici d'autres gestionnaires d'événements selon les besoins
    }

    private void emitEvent(String event, DTO data) {
        List<SocketIOClient> clientsCopy = new ArrayList<>(clients); // Créez une copie de la liste clients
        for (SocketIOClient client : clientsCopy) {
            client.sendEvent(event, data);
            System.out.println("Event " + event + " sent to client " + client.getSessionId() + " with data " + data);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void displayPlayerPlaysCard(Player player, Card card) {
        emitEvent("playerPlaysCard", new PlayerAndCardDTO(player, card));
    }

    @Override
    public void displayWinner(Player winner) {
        emitEvent("displayWinner", new WinnerDTO(winner, winner.getScore()));
    }

    @Override
    public void displayPlayerStartPlaying(Player player) {
        emitEvent("displayPlayerStartPlaying", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerPickCards(Player player, int numberOfCards) {
        emitEvent("displayPlayerPickCards", new PlayerPickCardsDTO(player, numberOfCards));
    }

    @Override
    public void displayPlayerPicksGold(Player player, int numberOfGold) {
        emitEvent("displayPlayerPicksGold", new PlayerPickGoldDTO(player, numberOfGold));
    }

    @Override
    public void displayPlayerChooseCharacter(Player player) {
        emitEvent("displayPlayerChooseCharacter", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerRevealCharacter(Player player) {
        // Ne sert à rien puisque envoie les mêmes infos que displayPlayerStartPlaying
    }

    @Override
    public void displayPlayerUseCondottiereDistrict(Player attacker, Player defender, District district) {
        emitEvent("displayPlayerUseCondottiereDistrict", new CondottiereEffectDTO(attacker, defender, district));
    }

    @Override
    public void displayPlayerScore(Player player) {
        emitEvent("displayPlayerScore", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerGetBonus(Player player, int pointsBonus, String bonusName) {
        emitEvent("displayPlayerGetBonus", new PlayerGetBonusDTO(player, pointsBonus, bonusName));
    }

    @Override
    public void displayGameFinished() {
        emitEvent("displayGameFinished", new EmptyDTO());
    }

    @Override
    public void displayPlayerUseAssassinEffect(Player player, Character target) {
        emitEvent("displayPlayerUseAssassinEffect", new MurdererEffectDTO(player, target));
    }

    // Implementez les autres méthodes de l'interface IView de la même manière

    @Override
    public void displayPlayerInfo(Player player) {
        // Affiche les informations du joueur
        // Ici, on pourrait envoyer les détails du joueur au client
        emitEvent("displayPlayerInfo", new PlayerDTO(player));
    }

    @Override
    public void displayUnusedCharacterInRound(Character character) {
        emitEvent("displayUnusedCharacterInRound", new CharacterDTO(character));
    }

    @Override
    public void displayRound(int roundNumber) {
        emitEvent("displayRound", new RoundNumberDTO(roundNumber));
    }

    @Override
    public void displayPlayerError(Player player, String message) {
        emitEvent("displayPlayerError", new PlayerAndMessageDTO(player, message));
    }

    @Override
    public void displayPlayerStrategy(Player player, String message) {
        emitEvent("displayPlayerStrategy", new PlayerAndMessageDTO(player, message));
    }

    @Override
    public void displayStolenCharacter(Character character) {
        emitEvent("displayStolenCharacter", new CharacterDTO(character));
    }

    @Override
    public void displayActualNumberOfGold(Player player) {
        emitEvent("displayActualNumberOfGold", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerUseMagicianEffect(Player player, Opponent targetPlayer) {
        emitEvent("displayPlayerUseMagicianEffect", new MagicianEffectDTO(player, targetPlayer));
    }

    @Override
    public void displayPlayerHasGotObservatory(Player player) {
        emitEvent("displayPlayerHasGotObservatory", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerUseThiefEffect(Player player) {
        emitEvent("displayPlayerUseThiefEffect", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerDiscardCard(Player player, Card card) {
        emitEvent("displayPlayerDiscardCard", new PlayerAndCardDTO(player, card));
    }

    @Override
    public void displayPlayerUseLaboratoryEffect(Player player) {
        emitEvent("displayPlayerUseLaboratoryEffect", new PlayerDTO(player));
    }

    @Override
    public void displayPlayerUseManufactureEffect(Player player) {
        emitEvent("displayPlayerUseManufactureEffect", new PlayerDTO(player));
    }

    @Override
    public void displayGoldCollectedFromDisctrictType(Player player, int nbGold, DistrictType districtType) {
        emitEvent("displayGoldCollectedFromDisctrictType", new PlayerCollectGoldDTO(player, nbGold, districtType));
    }

    @Override
    public void displayGameStuck() {
        emitEvent("displayGameStuck", new EmptyDTO());
    }
}