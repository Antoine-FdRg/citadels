package com.seinksansdoozebank.fr;

import com.seinksansdoozebank.fr.controller.Game;

public class Launcher {
    public static void main(String[] args) {
        Game game = new Game(4);
        game.run();
    }
}
