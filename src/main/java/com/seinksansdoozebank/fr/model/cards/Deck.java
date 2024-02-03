package com.seinksansdoozebank.fr.model.cards;

import java.util.*;

public class Deck {
    private final List<Card> cardsList;
    private final Random random;

    /**
     * Constructor which implements a new deck of 65 cards of district
     */
    public Deck() {
        this.cardsList = new ArrayList<>();
        random = new Random();
        fillDeck();
    }

    /**
     * We created the deck of 65 cards and then we shuffle it
     */
    private void fillDeck() {
        for (District district : District.values()) {
            //We take the ordinal which corresponds to a district and take the number of appearances
            int numberOfAppearance = district.getNumberOfAppearance();
            for (int j = 0; j < numberOfAppearance; j++) {
                //We add to the list the right number of the district called
                this.cardsList.add(new Card(district));
            }
        }
        shuffle();
    }

    /**
     * @return the last element of cardsList and we remove it
     */
    public Optional<Card> pick() {
        //On vérifie que la liste n'est pas vide
        if (cardsList.isEmpty()) {
            //On recrée le deck
            return Optional.empty();
        }
        //On renvoie la dernière carte district du paquet et on l'enlève du paquet.
        return Optional.of(cardsList.remove(cardsList.size() - 1));
    }

    /**
     * Allows to discard a district
     *
     * @param cardToDiscard the card to discard
     */
    public void discard(Card cardToDiscard) {
        this.cardsList.add(0, cardToDiscard);
    }


    /**
     * The method shuffle takes the list of cards and shuffles it
     */
    protected void shuffle() {
        //On commence par la dernière carte du paquet
        for (int i = cardsList.size() - 1; i >= 1; i--) {
            //on choisit un index au hasard parmi les autres éléments, cet index pourra prendre sa valeur entre 0 et i
            int j = random.nextInt(i + 1);
            //On échange l'élément à la i-ème place avec celui à la j-ème place
            Collections.swap(cardsList, i, j);
        }
    }

    /**
     * getter
     *
     * @return the list of cards
     */
    public List<Card> getDeck() {

        return cardsList;
    }

}
