package com.seinksansdoozebank.fr;

import com.seinksansdoozebank.fr.controller.Game;
import com.seinksansdoozebank.fr.view.Cli;
import com.seinksansdoozebank.fr.view.IView;

public class Launcher {
    public static void main(String[] args) {
        IView view = new Cli();
        Game game = new Game(4,view);
        game.run();
    }
}
