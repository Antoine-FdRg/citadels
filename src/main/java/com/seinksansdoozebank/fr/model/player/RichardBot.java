package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
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
        List<Character> copyCharacters = new ArrayList<>(characters);
        List<Character> orderedCharacters = new ArrayList<>();
        Assassin assassin = (Assassin) copyCharacters.stream().filter(c -> c.getRole() == Role.ASSASSIN).findFirst().orElseThrow();
        copyCharacters.remove(assassin);
        Magician magician = (Magician) copyCharacters.stream().filter(c -> c.getRole() == Role.MAGICIAN).findFirst().orElseThrow();
        copyCharacters.remove(magician);
        Merchant merchant = (Merchant) copyCharacters.stream().filter(c -> c.getRole() == Role.MERCHANT).findFirst().orElseThrow();
        copyCharacters.remove(merchant);
        Architect architect = (Architect) copyCharacters.stream().filter(c -> c.getRole() == Role.ARCHITECT).findFirst().orElseThrow();
        copyCharacters.remove(architect);
        Condottiere condottiere = (Condottiere) copyCharacters.stream().filter(c -> c.getRole() == Role.CONDOTTIERE).findFirst().orElseThrow();
        copyCharacters.remove(condottiere);
        Bishop bishop = (Bishop) copyCharacters.stream().filter(c -> c.getRole() == Role.BISHOP).findFirst().orElseThrow();
        copyCharacters.remove(bishop);
        List<Character> charactersRemaining = new ArrayList<>(copyCharacters);

        orderedCharacters.add(assassin);
        orderedCharacters.add(magician);
        orderedCharacters.add(merchant);
        orderedCharacters.add(architect);
        orderedCharacters.add(bishop);
        orderedCharacters.add(condottiere);
        orderedCharacters.addAll(charactersRemaining);

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

    /**
     * tells us if players with more gold than the bot exist
     *
     * @param opponents list of the opponents of the player
     * @return a boolean
     */
    protected boolean numberOfPlayerWithMoreGold(List<Opponent> opponents) {
        return opponents.stream().anyMatch(opponent -> this.getNbGold() < opponent.getNbGold());
    }

    /**
     * La méthode check si le joueur a plus de 7 cartes dans la main et si les autres joueurs ont des mains vides et renvoie un booléen.;
     * Dans ce cas, il choisit l'assassin.
     *
     * @return a boolean
     */
    protected boolean shouldChooseAssassin() {
        return getHand().size() >= 7 && numberOfEmptyHands(this.getOpponents()) >= 1;
    }

    /**
     * La méthode check si le joueur a une main vide
     * Dans ce cas, il choisit le magicien.
     *
     * @return a boolean
     */
    protected boolean shouldChooseMagician() {
        return this.getHand().isEmpty();
    }

    /**
     * La méthode check si le nombre d'or du joueur est inferieur ou égele à 1.
     * Dans ce cas, il choisit le marchand.
     *
     * @return a boolean
     */
    protected boolean shouldChooseMerchant() {
        return this.getNbGold() <= 1;
    }

    /**
     * La méthode check si le joueur peut poser deux districts en plus dans sa citadelle et si ses opposants ont plus d'or que lui.
     * Dans ce cas, il choisit l'architecte.
     *
     * @return a boolean
     */
    protected boolean shouldChooseArchitect() {
        return getPriceOfNumbersOfCheaperCards(2) <= this.getNbGold() && numberOfPlayerWithMoreGold(this.getOpponents());
    }


    /**
     * La méthode check si le joueur peut poser au moins un district
     * Dans ce cas, il choisit l'évêque.
     *
     * @return a boolean
     */
    protected boolean shouldChooseBishop() {
        return getPriceOfNumbersOfCheaperCards(1) <= this.getNbGold();
    }

    /**
     * La méthode check si le joueur ne peut pas construire de district.
     * Dans ce cas, il choisit le condottière.
     *
     * @return a boolean
     */
    protected boolean shouldChooseCondottiere() {
        return getPriceOfNumbersOfCheaperCards(1) > this.getNbGold();
    }

    /**
     * Cette méthode permet au joueur s'il est en position de poser son dernier district
     * dans sa cité de choisir soit l'assassin, soit l'évêque, soit le condottière.
     *
     * @param characters list of characters available
     * @return an optional of character
     */
    protected Optional<Character> shouldChooseBecauseLastCardToBuy(List<Character> characters) {
        if (isAboutToWin()) {
            return characters.stream().filter(c -> c.getRole() == Role.ASSASSIN || c.getRole() == Role.BISHOP || c.getRole() == Role.CONDOTTIERE).findFirst();
        }
        return Optional.empty();
    }


}
