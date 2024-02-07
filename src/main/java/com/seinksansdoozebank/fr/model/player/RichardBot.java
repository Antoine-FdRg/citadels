package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.King;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.MagicianTarget;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
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

    /**
     * Cette méthode nous permet de choisir notre personnage en prenant en compte les cas où un opposant est sur le point
     * de poser son dernier district et de gagner. On doit évaluer si l'opposant doit choisir
     * son caractère en premier deuxième ou troisième et faire en fonction
     * @param characters list of available characters
     * @param opponent the opponent who gets 7 districts in it citadel
     * @return an optional of the character that will be assigned to the current player
     */
    Optional<Character> chooseCharacterWHenOpponentHasOneDistrictLeft(List<Character> characters,Opponent opponent){
        //Si l'opposant est deuxième à choisir son role alors, on doit choisir l'assassin
        if( opponent.getPositionInDrawToPickACharacter()==1){
            return Optional.of(new Assassin());
        }
        //Cas où l'opposant est 3ème à choisir
        if(opponent.getPositionInDrawToPickACharacter()==2) {
            if (characters.contains(new King())) {
                return Optional.of(new King());
            }
            if (characters.contains(new Assassin()) && characters.contains(new Bishop()) && characters.contains(new Condottiere())) {
                return whenCharacterContainsBishopCondottiereAssassin();
            }
            if (!characters.contains(new Condottiere())) {
                return whenCharacterDoesNotContainCondottiere();
            } else if(!characters.contains(new Bishop())){
                return whenCharacterDoesNotContainBishop();
            }
            else {
                return whenCharacterDoesNotContainAssassin();
            }
        }
        return Optional.of(new Assassin());
    }

    /**
     * Cas où l'évêque, l'assassin, condottière n'a pas été pris
     * Prendre en compte le rang du perso actuel
     * @return an optional of the character
     */
    Optional<Character> whenCharacterContainsBishopCondottiereAssassin(){
        if(this.getPositionInDrawToPickACharacter()==0){
            return Optional.of(new Condottiere());
        } else if (this.getPositionInDrawToPickACharacter()==1){
            return Optional.of(new Assassin());
        }
        return Optional.of(new Condottiere());
    }

    /**
     * Cas où le condottière n'est pas présente dans la liste des perso disponibles
     * @return an optional of the character
     */
    Optional<Character> whenCharacterDoesNotContainCondottiere(){
        if(this.getPositionInDrawToPickACharacter()==0){
            return Optional.of(new Assassin());
        } else if (this.getPositionInDrawToPickACharacter()==1){
            return Optional.of(new Magician());
        }
        return Optional.of(new Assassin());
    }

    /**
     * Cas où l'évêque n'est pas présente dans la liste des perso disponibles
     * @return an optional of the character
     */
    Optional<Character> whenCharacterDoesNotContainBishop(){
        if(this.getPositionInDrawToPickACharacter()==0){
            return Optional.of(new Assassin());
        } else if (this.getPositionInDrawToPickACharacter()==1){
            return Optional.of(new Condottiere());
        }
        return Optional.of(new Assassin());
    }

    /**
     * Cas où l'assassin n'est pas présente dans la liste des perso disponibles
     * @return an optional of the character
     */
    Optional<Character> whenCharacterDoesNotContainAssassin(){
        if(this.getPositionInDrawToPickACharacter()==0){
            return Optional.of(new Condottiere());
        } else if (this.getPositionInDrawToPickACharacter()==1){
            return Optional.of(new Bishop());
        }
        return Optional.of(new Condottiere());
    }

    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        Optional<Character> optionalCharacter ;
        Optional<Opponent> optionalOpponent=getOpponents().stream().filter(opponent -> opponent.getCitadel().size()==7).findFirst();
        if(optionalOpponent.isPresent()){
           optionalCharacter=chooseCharacterWHenOpponentHasOneDistrictLeft(characters,optionalOpponent.get());
           if(optionalCharacter.isPresent() && characters.contains(optionalCharacter.get())){
               return optionalCharacter.get();
           }
        }
        List<Character> orderedCharacters = ordinateCharacters(characters);
        optionalCharacter = shouldChooseBecauseLastCardToBuy(characters);
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
     */
    @Override
    public MagicianTarget useEffectMagician() {
        Opponent leadingOpponent = StrategyUtils.getLeadingOpponent(this);
        if (leadingOpponent.isAboutToWin()) {
            return new MagicianTarget(leadingOpponent, null);
        }
        Optional<Opponent> playerWithMostDistricts = this.getOpponents().stream()
                .max(Comparator.comparingInt(Opponent::getHandSize));
        return new MagicianTarget(playerWithMostDistricts.orElse(null), null);
    }


}
