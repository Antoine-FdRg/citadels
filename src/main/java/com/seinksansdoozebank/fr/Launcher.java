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
import com.seinksansdoozebank.fr.view.socket.WebSocketView;

import java.util.concurrent.CountDownLatch;

public class Launcher {
    public static void main(String[] args) throws InterruptedException {
        //Setup du serveur
        final CountDownLatch latch = new CountDownLatch(1); // Crée un CountDownLatch qui attend un client
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(5001);
        final SocketIOServer server = new SocketIOServer(config);
        WebSocketView view = new WebSocketView(server, latch);
        server.start();

        // Attente de la connexion du client
        System.out.println("En attente de la connexion du client...");
        latch.await();

        // Lancement du jeu
        Thread.sleep(500);
        Game game = new GameBuilder(view, new Deck())
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
