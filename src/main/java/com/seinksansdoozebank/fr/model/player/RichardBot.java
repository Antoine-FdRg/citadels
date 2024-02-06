package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.model.player.custombot.strategies.StrategyUtils;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RichardBot extends SmartBot {

    public RichardBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    boolean anOpponentIsAboutToWin() {
        return this.getOpponents().stream().anyMatch(Opponent::isAboutToWin);
    }

    @Override
    protected Optional<Character> chooseThiefTarget() {
        List<Role> charactersInTheRound = this.getAvailableCharacters().stream().map(Character::getRole).toList();
        if (this.anOpponentIsAboutToWin()) {
            if (charactersInTheRound.contains(Role.BISHOP)) {
                return Optional.ofNullable(StrategyUtils.getCharacterFromRoleInLIst(Role.BISHOP, this.getAvailableCharacters()));
            } else if (charactersInTheRound.contains(Role.CONDOTTIERE)) {
                return Optional.ofNullable(StrategyUtils.getCharacterFromRoleInLIst(Role.CONDOTTIERE, this.getAvailableCharacters()));
            }
        }
        return useSuperChoseThiefEffect();
    }

    Optional<Character> useSuperChoseThiefEffect() {
        return super.chooseThiefTarget();
    }

    @Override
    protected Character chooseAssassinTarget() {
        List<Character> charactersList = this.getAvailableCharacters();
        // Conditions spécifiques pour Voleur et Condottiere
        Character target = null;
        for (Character character : charactersList) {
            if (character.getRole() == Role.THIEF && (shouldPreventWealth() || thinkThiefHasBeenChosenByTheLeadingOpponent())
                    || character.getRole() == Role.CONDOTTIERE && (this.isAboutToWin() || thinkCondottiereHasBeenChosenByTheLeadingOpponent())) {
                target = character;
            }
        }
        if (target == null) {
            return super.chooseAssassinTarget();
        }
        return target;
    }

    /**
     * Vérifie si un adversaire a un grand nombre de pièces d'or (7 ou plus)
     *
     * @return true si un adversaire a 7 pièces d'or ou plus, false sinon
     */
    boolean shouldPreventWealth() {
        return this.getOpponents().stream().anyMatch(opponent -> opponent.getNbGold() > 7);
    }

    /**
     * Check if the Condottiere will be chosen by the leading opponent
     *
     * @return true if the player think Condottiere has been chosen by the leading opponent, false otherwise
     */
    boolean thinkCondottiereHasBeenChosenByTheLeadingOpponent() {
        // if leadingOpponent is about to win, he will choose Condottiere or Bishop, but the bishop is not killable
        return StrategyUtils.getLeadingOpponent(this).isAboutToWin();
    }

    /**
     * Check if the Thief has been chosen by the leading opponent
     *
     * @return true if the player think Thief has been chosen by the opponent about to win, false otherwise
     */
    boolean thinkThiefHasBeenChosenByTheLeadingOpponent() {
        Thief thief = new Thief();
        if (this.getCharactersNotInRound().contains(thief)) { // if thief is not in the round, we return false (because he can't be chosen)
            return false;
        }
        if (this.getCharactersSeenInRound().contains(thief)) { // if thief has been seen (means that he has been chosen after the player)
            return this.getOpponentsWhichHasChosenCharacterAfter().stream().anyMatch(Opponent::isAboutToWin); // we check if the opponent which is about to win has chosen after the player (could mean that he has chosen the thief)
        } else {
            return this.getOpponentsWhichHasChosenCharacterBefore().stream().anyMatch(Opponent::isAboutToWin); // if the thief has not been seen, we check if the opponents which has chosen before the player are about to win (could mean that the thief has been chosen by one of them)
        }
    }

    /**
     * Get the opponents which has chosen their character before the player
     * make difference between this.getOpponents() and this.getOpponentsWhichHasChosenCharacterBefore()
     *
     * @return the opponents which has chosen their character before the player
     */
    List<Opponent> getOpponentsWhichHasChosenCharacterAfter() {
        return this.getOpponents().stream().filter(opponent -> !this.getOpponentsWhichHasChosenCharacterBefore().contains(opponent)).toList();
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
    List<Character> ordinateCharacters(List<Character> characters) {
        List<Character> copyCharacters = new ArrayList<>(characters);
        List<Character> orderedCharacters = new ArrayList<>();

        Optional<Character> optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.ASSASSIN).findFirst();
        optionalCharacter.ifPresent(assassin -> {
            orderedCharacters.add(assassin);
            copyCharacters.remove(assassin);
        });
        optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.MAGICIAN).findFirst();
        optionalCharacter.ifPresent(magician -> {
            orderedCharacters.add(magician);
            copyCharacters.remove(magician);
        });
        optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.MERCHANT).findFirst();
        optionalCharacter.ifPresent(merchant -> {
            orderedCharacters.add(merchant);
            copyCharacters.remove(merchant);
        });
        optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.ARCHITECT).findFirst();
        optionalCharacter.ifPresent(architect -> {
            orderedCharacters.add(architect);
            copyCharacters.remove(architect);
        });
        optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.BISHOP).findFirst();
        optionalCharacter.ifPresent(bishop -> {
            orderedCharacters.add(bishop);
            copyCharacters.remove(bishop);
        });
        optionalCharacter = copyCharacters.stream().filter(c -> c.getRole() == Role.CONDOTTIERE).findFirst();
        optionalCharacter.ifPresent(condottiere -> {
            orderedCharacters.add(condottiere);
            copyCharacters.remove(condottiere);
        });
        List<Character> charactersRemaining = new ArrayList<>(copyCharacters);

        orderedCharacters.addAll(charactersRemaining);

        return orderedCharacters;
    }


    /**
     * find the number of players with empty hands in the game
     *
     * @param opponents list of the opponents of the player
     * @return the number of players with empty hands in the game
     */
    int numberOfEmptyHands(List<Opponent> opponents) {
        return (int) opponents.stream().filter(player -> player.getHandSize() == 0).count();
    }

    /**
     * tells us if players with more gold than the bot exist
     *
     * @param opponents list of the opponents of the player
     * @return a boolean
     */
    boolean numberOfPlayerWithMoreGold(List<Opponent> opponents) {
        return opponents.stream().anyMatch(opponent -> this.getNbGold() < opponent.getNbGold());
    }

    /**
     * La méthode check si le joueur a plus de 7 cartes dans la main et si les autres joueurs ont des mains vides et renvoie un booléen.
     * Dans ce cas, il choisit l'assassin.
     *
     * @return a boolean
     */
    boolean shouldChooseAssassin() {
        return getHand().size() >= 7 && numberOfEmptyHands(this.getOpponents()) >= 1;
    }

    /**
     * La méthode check si le joueur a une main vide
     * Dans ce cas, il choisit le magicien.
     *
     * @return a boolean
     */
    boolean shouldChooseMagician() {
        return this.getHand().isEmpty();
    }

    /**
     * La méthode check si le nombre d'or du joueur est inférieur ou égal à 1.
     * Dans ce cas, il choisit le marchand.
     *
     * @return a boolean
     */
    boolean shouldChooseMerchant() {
        return this.getNbGold() <= 1;
    }

    /**
     * La méthode check si le joueur peut poser deux districts en plus dans sa citadelle et si ses opposants ont plus d'or que lui.
     * Dans ce cas, il choisit l'architecte.
     *
     * @return a boolean
     */
    boolean shouldChooseArchitect() {
        return getPriceOfNumbersOfCheaperCards(2) <= this.getNbGold() && numberOfPlayerWithMoreGold(this.getOpponents());
    }


    /**
     * La méthode check si le joueur peut poser au moins un district
     * Dans ce cas, il choisit l'évêque.
     *
     * @return a boolean
     */
    boolean shouldChooseBishop() {
        return getPriceOfNumbersOfCheaperCards(1) <= this.getNbGold();
    }

    /**
     * La méthode check si le joueur ne peut pas construire de district.
     * Dans ce cas, il choisit le condottière.
     *
     * @return a boolean
     */
    boolean shouldChooseCondottiere() {
        return getPriceOfNumbersOfCheaperCards(1) > this.getNbGold();
    }

    /**
     * Cette méthode permet au joueur s'il est en position de poser son dernier district
     * dans sa cité de choisir soit l'assassin, soit l'évêque, soit le condottière.
     *
     * @param characters list of characters available
     * @return an optional of character
     */
    Optional<Character> shouldChooseBecauseLastCardToBuy(List<Character> characters) {
        if (isAboutToWin()) {
            return characters.stream().filter(c -> c.getRole() == Role.ASSASSIN || c.getRole() == Role.BISHOP || c.getRole() == Role.CONDOTTIERE).findFirst();
        }
        return Optional.empty();
    }

    /**
     * Use the magician effect to switch hand with the opponent which has the most districts
     *
     * @param magician the magician character
     */
    @Override
    protected void useEffectMagician(Magician magician) {
        Opponent leadingOpponent = StrategyUtils.getLeadingOpponent(this);
        if (leadingOpponent.isAboutToWin()) {
            magician.useEffect(leadingOpponent, null);
            return;
        }
        Optional<Opponent> playerWithMostDistricts = this.getOpponents().stream()
                .max(Comparator.comparingInt(Opponent::getHandSize));
        playerWithMostDistricts.ifPresent(opponent -> magician.useEffect(opponent, null));
    }


}
