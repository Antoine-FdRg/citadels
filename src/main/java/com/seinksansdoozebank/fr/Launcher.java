package com.seinksansdoozebank.fr;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.controller.GameBuilder;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.characterchoosing.ChoosingCharacterToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.condottiereeffect.UsingCondottiereEffectToTargetFirstPlayer;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.murderereffect.UsingMurdererEffectToFocusRusher;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.thiefeffect.UsingThiefEffectToFocusRusher;
import com.seinksansdoozebank.fr.view.WebSocketView;

import java.util.concurrent.CountDownLatch;

public class Launcher {
    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1); // Crée un CountDownLatch qui attend un client

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(5001);
        final SocketIOServer server = new SocketIOServer(config);
        server.addConnectListener(client -> {
            System.out.println("Client connecté: " + client.getSessionId());
            latch.countDown(); // Décrémente le compteur, indiquant qu'un client est connecté
        });
        server.start();
        System.out.println("En attente de la connexion du client...");
        latch.await();
        Game game = new GameBuilder(new WebSocketView(server), new Deck())
                .addRandomBot()
                .addSmartBot()
                .addRandomBot()
                .addCustomBot(null, new ChoosingCharacterToTargetFirstPlayer(),
                        new UsingThiefEffectToFocusRusher(),
                        new UsingMurdererEffectToFocusRusher(),
                        new UsingCondottiereEffectToTargetFirstPlayer())
                .build();
        game.run();
        server.stop();
    }
}
