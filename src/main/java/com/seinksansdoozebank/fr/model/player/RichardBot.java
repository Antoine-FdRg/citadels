package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RichardBot extends SmartBot {

    public RichardBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        List<Character> orderedCharacters = ordinateCharacters(characters);
        Optional<Character> optionalCharacter = shouldChooseBecauseLastCardToBuy(characters);
        if (optionalCharacter.isPresent()) {
            return optionalCharacter.get();
        }
        for (Character character : orderedCharacters) {
            switch (character.getRole()) {
                case ASSASSIN -> {
                    if (shouldChooseAssassin()) {
                        return character;
                    }
                }
                case MAGICIAN -> {
                    if (shouldChooseMagician()) {
                        return character;
                    }
                }
                case MERCHANT -> {
                    if (shouldChooseMerchant()) {
                        return character;
                    }
                }
                case ARCHITECT -> {
                    if (shouldChooseArchitect()) {
                        return character;
                    }
                }
                case BISHOP -> {
                    if (shouldChooseBishop()) {
                        return character;
                    }
                }
                case CONDOTTIERE -> {
                    if (shouldChooseCondottiere()) {
                        return character;
                    }
                }
                default -> {
                    break;
                }
            }
        }
        return super.chooseCharacterImpl(characters);
    }
    /**
     * @param characters list of available characters
     * @return the list of available characters ordered like we want
     */
    protected List<Character> ordinateCharacters(List<Character> characters) {
        List<Character> orderedCharacters = new ArrayList<>(characters);
        int compteur = 5;
        for (Character character : characters) {
            switch (character.getRole()) {
                case ASSASSIN -> orderedCharacters.add(0, character);
                case MAGICIAN -> orderedCharacters.add(1, character);
                case MERCHANT -> orderedCharacters.add(2, character);
                case ARCHITECT -> orderedCharacters.add(3, character);
                case BISHOP -> orderedCharacters.add(7, character);
                case CONDOTTIERE -> orderedCharacters.add(5, character);
                default -> {
                    orderedCharacters.add(compteur, character);
                    compteur += 1;
                }
            }
        }
        return orderedCharacters;
    }


    /**
     * find the number of players with empty hands in the game
     *
     * @param opponents list of the opponents of the player
     * @return the number of players with empty hands in the game
     */
    protected int numberOfEmptyHands(List<Opponent> opponents) {
        return (int) opponents.stream().filter(player -> player.getHandSize() == 0).count();
    }







}
