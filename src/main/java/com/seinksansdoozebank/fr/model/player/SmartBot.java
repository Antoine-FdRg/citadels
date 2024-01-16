package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.District;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Bishop;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Condottiere;
import com.seinksansdoozebank.fr.model.character.commoncharacters.Merchant;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Architect;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Assassin;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Represents a smart bot which will try to build the cheaper district
 * in its hand in order to finish its citadel as fast as possible
 */
public class SmartBot extends Player {

    public SmartBot(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    @Override
    public void play() {
        if(this.getCharacter().isDead()){
            throw new IllegalStateException("The player is dead, he can't play.");
        }
        view.displayPlayerStartPlaying(this);
        view.displayPlayerRevealCharacter(this);
        view.displayPlayerInfo(this);
        Optional<Card> optChosenCard = this.chooseCard();
        this.useEffect();
        if (optChosenCard.isPresent()) {
            Card choosenCard = optChosenCard.get();
            if (this.canPlayCard(choosenCard)) {
                if (this.character instanceof Architect) {
                    this.pickSomething();
                    useEffectOfTheArchitect();
                } else {
                    view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
                    if (character instanceof CommonCharacter commonCharacter) {
                        commonCharacter.goldCollectedFromDisctrictType();
                    }
                    this.pickSomething();
                }
            } else {
                if (character instanceof CommonCharacter commonCharacter) {
                    commonCharacter.goldCollectedFromDisctrictType();
                }
                if (this.canPlayCard(choosenCard)) {
                    view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
                } else {
                    this.pickGold();
                    view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
                }
            }
        } else {//la main est vide
            this.pickTwoCardKeepOneDiscardOne(); //
            view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
        }
        view.displayPlayerInfo(this);
    }

    @Override
    protected void pickSomething() {
        Optional<Card> optCheaperPlayableCard = this.chooseCard();
        if (optCheaperPlayableCard.isEmpty()) { //s'il n'y a pas de district le moins cher => la main est vide
            this.pickTwoCardKeepOneDiscardOne(); // => il faut piocher
        } else { //s'il y a un district le moins cher
            Card cheaperCard = optCheaperPlayableCard.get();
            if (this.getNbGold() < cheaperCard.getDistrict().getCost()) { //si le joueur n'a pas assez d'or pour acheter le district le moins cher
                this.pickGold(); // => il faut piocher de l'or
            } else { //si le joueur a assez d'or pour construire le district le moins cher
                this.pickTwoCardKeepOneDiscardOne(); // => il faut piocher un quartier pour savoir combien d'or sera nécessaire
            }
        }
    }

    @Override
    protected void pickTwoCardKeepOneDiscardOne() {
        this.view.displayPlayerPickCards(this, 1);
        //Pick two district
        Card card1 = this.deck.pick();
        Card card2 = this.deck.pick();
        //Keep the cheaper one and discard the other one
        if (card1.getDistrict().getCost() < card2.getDistrict().getCost()) {
            this.hand.add(card1);
            this.deck.discard(card2);
        } else {
            this.hand.add(card2);
            this.deck.discard(card1);
        }
    }

    /**
     * Choose the cheaper card among those wich are not already ine teh citadel OR by trying to play a DistrictType not already in the citadel if it has a CommonCharacter
     * @return the chosenCard
     */
    @Override
    protected Optional<Card> chooseCard() {
        //Gathering districts which are not already built in player's citadel
        List<Card> notAlreadyPlayedCardList = this.hand.stream().filter(d -> !this.getCitadel().contains(d)).toList();
        if (this.character instanceof CommonCharacter commonCharacter) {
            DistrictType target = commonCharacter.getTarget();
            Optional<Card> optCard = notAlreadyPlayedCardList.stream()
                    .filter(card -> card.getDistrict().getDistrictType() == target) // filter the cards that are the same as the character's target
                    .min(Comparator.comparing(card -> card.getDistrict().getCost())); // choose the cheaper one
            if (optCard.isPresent()) {
                return optCard;
            }
        }
        //Choosing the cheaper one
        return this.getCheaperCard(notAlreadyPlayedCardList);
    }

    /**
     * Returns the cheaper district in the hand if there is one or an empty optional
     *
     * @return the cheaper district in the hand if there is one or an empty optional
     */
    protected Optional<Card> getCheaperCard(List<Card> notAlreadyPlayedCardList) {
        return notAlreadyPlayedCardList.stream().min(Comparator.comparing(card -> card.getDistrict().getCost()));
    }

    @Override
    public Character chooseCharacter(List<Character> characters) {
        // Choose the character by getting the frequency of each districtType in the citadel
        // and choosing the districtType with the highest frequency for the character

        List<DistrictType> districtTypeFrequencyList = getDistrictTypeFrequencyList(this.getCitadel());
        if (!districtTypeFrequencyList.isEmpty()) {
            // Choose the character with the mostOwnedDistrictType
            for (DistrictType districtType : districtTypeFrequencyList) {
                for (Character character : characters) {
                    if (character instanceof CommonCharacter commonCharacter && (commonCharacter.getTarget() == districtType)) {
                        this.character = commonCharacter;
                        this.character.setPlayer(this);
                        this.view.displayPlayerChooseCharacter(this);
                        return this.character;
                    }
                }
            }
        }
        // If no character has the mostOwnedDistrictType, choose a random character
        this.character = characters.get(random.nextInt(characters.size()));
        this.character.setPlayer(this);
        characters.remove(this.character);
        this.view.displayPlayerChooseCharacter(this);
        return this.character;
    }

    /**
     * Returns a list of districtType sorted by frequency in the citadel
     *
     * @param citadel the citadel of the player
     * @return a list of districtType sorted by frequency in the citadel
     */
    protected List<DistrictType> getDistrictTypeFrequencyList(List<Card> citadel) {
        return citadel.stream()
                .map(card -> card.getDistrict().getDistrictType())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .toList();
    }

    protected void useEffect() {
        if (this.character instanceof Merchant merchant) {
            merchant.useEffect();
        } else if (this.character instanceof Assassin assassin) {
            Character target = this.choseAssassinTarget();
            assassin.useEffect(target);
            view.displayPlayerUseAssasinEffect(this,target);
        }
        // The strategy of the smart bot for condottiere will be to destroy the best district of the player which owns the highest number of districts
        else if (this.character instanceof Condottiere) {
            useEffectOfTheCondottiere();
        } else if (this.character instanceof Architect) {
            this.useEffectArchitectPickCards();
        }
    }

    protected void useEffectOfTheCondottiere() {
        // Get the player with the most districts
        Optional<Player> playerWithMostDistricts = this.getOpponents().stream() // get players is not possible because it will create a link between model and controller
                .max(Comparator.comparing(player -> player.getCitadel().size()));
        if (playerWithMostDistricts.isEmpty() || playerWithMostDistricts.get().character instanceof Bishop) {
            return;
        }
        // Sort the districts of the player by cost
        List<Card> cardOfPlayerSortedByCost = playerWithMostDistricts.get().getCitadel().stream()
                .sorted(Comparator.comparing(card -> card.getDistrict().getCost()))
                .toList();
        // Destroy the district with the highest cost, if not possible destroy the district with the second highest cost, etc...
        for (Card card : cardOfPlayerSortedByCost) {
            if (this.getNbGold() >= card.getDistrict().getCost() + 1) {
                Condottiere condottiere= (Condottiere) this.character;
                try {
                    condottiere.useEffect(playerWithMostDistricts.get().getCharacter(), card.getDistrict());
                    return;
                } catch (IllegalArgumentException e) {
                    view.displayPlayerStrategy(this, this + " ne peut pas détruire le quartier " + card.getDistrict().getName() + " du joueur " + playerWithMostDistricts.get().id + ", il passe donc à la carte suivante");
                }
            }
        }
    }



    /**
     * Il finit sa citadelle s'il peut en un coup, sinon il pose une merveille, sinon il complète les 5
     * couleurs de districtType sinon il joue comme un joueur normal
     */
    protected void useEffectOfTheArchitect() {

        int numberOfCardsNeededToFinishTheGame = 8 - this.getCitadel().size();
        //On regarde s'il peut finir la partie en un coup en vérifiant si la citadelle a plus de 4 cartes, si dans sa main il a au moins 3 cartes
        //On vérifie s'il peut acheter les x districts manquant en choisissant les moins chèrs
        if (this.getCitadel().size() >= 5 && this.getHand().size() >= 3 && getPriceOfNumbersOfCheaperCards(numberOfCardsNeededToFinishTheGame) >= this.getNbGold()) {
            view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
        } else {
            //on vérifie s'il y a une merveille dans sa main, si oui et qu'il peut la jouer alors il le fait
            Optional<Card> prestigeCard = this.getHand().stream().filter(card -> card.getDistrict().getDistrictType() == DistrictType.PRESTIGE).findFirst();
            if (prestigeCard.isPresent() && canPlayCard(prestigeCard.get())) {
                view.displayPlayerPlaysCard(this, playCard(prestigeCard.get()));
            } else if (!this.hasFiveDifferentDistrictTypes()) {
                //il cherche à avoir les 5 districts de couleur dans sa citadelle sinon
                architectTryToCompleteFiveDistrictTypes();
            } else {
                //Il joue comme un joueur normal
                view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
            }
        }
    }


    /**
     * @param numberCards the number of cards needed
     * @return the price of all the cards needed
     */
    public int getPriceOfNumbersOfCheaperCards(int numberCards) {
        this.getHand().sort(Comparator.comparing(card -> card.getDistrict().getCost()));
        return this.getHand().stream().limit(numberCards).mapToInt(card -> card.getDistrict().getCost()).sum();
    }

    /**
     * L'architecte essaye de compléter au maxim le nombre de couleurs de district dans sa citadelle
     */
    public void architectTryToCompleteFiveDistrictTypes() {
        //Création de la liste des cartes qu'il pourrait poser de sa main dans la citadelle intéressante pour lui en appelant la liste des
        //districtType manquant
        List<DistrictType> missingDistrictTypeInCitadel = findDistrictTypesMissingInCitadel();
        List<Card> cardNeeded = this.getHand().stream()
                .filter(card -> missingDistrictTypeInCitadel.contains(card.getDistrict().getDistrictType()))
                .toList();
        cardNeeded = new ArrayList<>(cardNeeded);

        int numberOfCards = 0;
        int i = 0;
        while (i < 3) {
            Optional<Card> optionalChosenCard = getCheaperCard(cardNeeded);
            if (optionalChosenCard.isPresent()) {
                Card cardChosen = optionalChosenCard.get();
                if (numberOfCards < 3 && (canPlayCard(cardChosen))) {
                    view.displayPlayerPlaysCard(this, playCard(cardChosen));
                    numberOfCards++;
                    cardNeeded.remove(cardChosen);
                }
            }
            i++;
        }
        //S'il n'y a aucune carte disponible ou bien qu'il ne peut en poser aucune alors il joue comme un joueur normal
        if (numberOfCards == 0) {
            view.displayPlayerPlaysCard(this, this.playCards(this.getNbDistrictsCanBeBuild()));
        }
    }



    /**
     * Returns the target of the assassin chosen by using the strength of characters or randomly if no "interesting" character has been found
     * @return the target of the assassin
     */
    protected Character choseAssassinTarget() {
        List<Role> roleInterestingToKill = new ArrayList<>(List.of(Role.ARCHITECT, Role.MERCHANT, Role.KING));
        Collections.shuffle(roleInterestingToKill);
        Character target = null;
        List<Character> charactersList = this.getOpponents().stream().map(Player::getCharacter).toList();
        for (Role role : roleInterestingToKill) {
            for (Character character : charactersList) {
                if (character.getRole() == role) {
                    target = character;
                    break;
                }
            }
        }
        if(target == null) {
            target = charactersList.get(random.nextInt(charactersList.size()));
        }
        return target;
    }

    public void chooseColorCourtyardOfMiracle() {
        // if the player has all different district types except one DistrictType, the bot will choose the missing one
        List<DistrictType> listDifferentDistrictType = getDistrictTypeFrequencyList(this.getCitadel());
        if (listDifferentDistrictType.size() == 4) {
            for (DistrictType districtType : DistrictType.values()) {
                if (!listDifferentDistrictType.contains(districtType)) {
                    this.getCitadel().stream()
                            .filter(card -> card.getDistrict().equals(District.COURTYARD_OF_MIRACLE))
                            .forEach(card -> this.setColorCourtyardOfMiracleType(districtType));
                    return;
                }
            }
        }
        // Do nothing otherwise
    }
    @Override
    public String toString() {
        return "Le bot malin " + this.id;
    }

}
