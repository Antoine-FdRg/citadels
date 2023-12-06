package com.seinksansdoozebank.fr.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<District> districtList;
    private static final int NUMBER_OF_CARDS = 65;


    /**
     * Constructor which implements a new deck of 65 districts
     */
    public Deck() {
        this.districtList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            this.districtList.add(new District(random.nextInt(1, 5)));
        }
        shuffle();
    }

    /**
     * @return the last element of districtList
     */
    public District pick() {
        //On vérifie que la liste n'est pas vide
        if (districtList.isEmpty()) {
            //TODO milestone 2 remettre la fausse dans la liste de District
            //On recrée le deck
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_CARDS; i++) {
                this.districtList.add(new District(random.nextInt(1, 5)));
            }
            shuffle();
        }
        //On récupère le dernier district de la liste
        District districtToBePicked = districtList.get(districtList.size() - 1);
        //On enlève à la liste le district pioché
        districtList.remove(districtList.size() - 1);
        //On renvoie le district pioché
        return districtToBePicked;
    }

    /**
     * The method shuffle takes the list of districts and shuffles it
     */
    public void shuffle() {
        Random random = new Random();
        //On commence par la dernière carte du paquet
        for (int i = districtList.size() - 1; i >= 1; i--) {
            //on choisit un index au hasard parmi les autres éléments
            int j = random.nextInt(i + 1);
            //On échange l'élément à la i-ème place avec celui à la j-ème place
            Collections.swap(districtList, i, j);
        }
    }

    /**
     * getter
     *
     * @return the deck of 65 districts
     */
    public List<District> getDeck() {
        return districtList;
    }

}
