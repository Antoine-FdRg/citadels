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
import com.seinksansdoozebank.fr.model.character.specialscharacters.Magician;
import com.seinksansdoozebank.fr.model.character.specialscharacters.Thief;
import com.seinksansdoozebank.fr.view.IView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
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
    public void playARound() {
        this.useEffect();
        if (!this.getHand().isEmpty()) { // s'il a des cartes en main
            this.playWhenHandIsNotEmpty();
        } else { //s'il n'a pas de cartes en main
            this.pickCardsKeepSomeAndDiscardOthers(); //
            this.playCards(this.getNbDistrictsCanBeBuild());
        }
    }

    private void playWhenHandIsNotEmpty() {
        if (this.hasACardToPlay()) { // s'il y a une carte à jouer
            if (this.character instanceof Architect) {
                this.pickSomething();
                useEffectOfTheArchitect();
            } else {
                this.playCards(this.getNbDistrictsCanBeBuild()); //il joue
                this.useCommonCharacterEffect();
                this.pickSomething(); //il pioche quelque chose
            }
        } else {
            this.useCommonCharacterEffect();
            if (this.hasACardToPlay()) {
                this.playCards(this.getNbDistrictsCanBeBuild());
                pickSomething();
            } else {
                pickGold();
                if (this.hasACardToPlay()) {
                    this.playCards(this.getNbDistrictsCanBeBuild());
                }
            }
        }
    }

    @Override
    protected void pickSomething() {
        Optional<Card> optCheaperPlayableCard = this.chooseCard();
        if (optCheaperPlayableCard.isEmpty()) { //s'il n'y a pas de district le moins cher => la main est vide
            this.pickCardsKeepSomeAndDiscardOthers(); // => il faut piocher
        } else { //s'il y a un district le moins cher
            Card cheaperCard = optCheaperPlayableCard.get();
            if (this.getNbGold() < cheaperCard.getDistrict().getCost()) { //si le joueur n'a pas assez d'or pour acheter le district le moins cher
                this.pickGold(); // => il faut piocher de l'or
            } else { //si le joueur a assez d'or pour construire le district le moins cher
                this.pickCardsKeepSomeAndDiscardOthers(); // => il faut piocher un quartier pour savoir combien d'or sera nécessaire
            }
        }
    }


    /**
     * On choisit ici la carte qui coùte la moins chère des cartes proposées
     *
     * @param pickedCards the cards picked
     * @return the card that will be kept
     */
    @Override
    protected Card keepOneDiscardOthers(List<Card> pickedCards) {
        Optional<Card> cardKept = pickedCards.stream().min(Comparator.comparing(card -> card.getDistrict().getCost()));
        return cardKept.orElse(null);
    }

    /**
     * Choose the cheaper card among those wich are not already in the citadel OR by trying to play a DistrictType not already in the citadel if it has a CommonCharacter
     *
     * @return the chosenCard
     */
    @Override
    protected Optional<Card> chooseCard() {
        //Gathering districts which are not already built in player's citadel
        List<Card> notAlreadyPlayedCardList = this.getHand().stream().filter(d -> !this.getCitadel().contains(d)).toList();
        Optional<Card> cardToPlay;
        if (this.character instanceof CommonCharacter commonCharacter) {
            DistrictType target = commonCharacter.getTarget();
            cardToPlay = notAlreadyPlayedCardList.stream()
                    .filter(card -> card.getDistrict().getDistrictType() == target) // filter the cards that are the same as the character's target
                    .min(Comparator.comparing(card -> card.getDistrict().getCost())); // choose the cheaper one
        } else {
            cardToPlay = this.getCheaperCard(notAlreadyPlayedCardList);
        }
        if (cardToPlay.isPresent() && this.canPlayCard(cardToPlay.get())) {
            return cardToPlay;
        } else {
            return this.getCheaperCard(notAlreadyPlayedCardList);
        }
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
            view.displayPlayerUseAssassinEffect(this, target);
        }
        // The strategy of the smart bot for condottiere will be to destroy the best district of the player which owns the highest number of districts
        else if (this.character instanceof Condottiere condottiere) {
            useEffectCondottiere(condottiere);
        } else if (this.character instanceof Architect) {
            this.useEffectArchitectPickCards();
        } else if (this.getCharacter() instanceof Thief thief) {
            this.useEffectThief(thief);
        } else if (this.character instanceof Magician magician) {
            this.useEffectMagician(magician);
        }
    }

    @Override
    protected void useEffectCondottiere(Condottiere condottiere) {
        // Get the player with the most districts
        Optional<Opponent> playerWithMostDistricts = this.getOpponents().stream() // get players is not possible because it will create a link between model and controller
                .filter(opponent -> !(opponent.getOpponentCharacter() instanceof Bishop)) // can't destroy the districts of the bishop
                .max(Comparator.comparing(player -> player.getCitadel().size()));
        if (playerWithMostDistricts.isEmpty()) {
            return;
        }
        // Sort the districts of the player by cost
        List<Card> cardOfPlayerSortedByCost = playerWithMostDistricts.get().getCitadel().stream()
                .sorted(Comparator.comparing(card -> card.getDistrict().getCost()))
                .toList();
        // Destroy the district with the highest cost, if not possible destroy the district with the second highest cost, etc...
        for (Card card : cardOfPlayerSortedByCost) {
            if (this.getNbGold() >= card.getDistrict().getCost() - 1) {
                try {
                    condottiere.useEffect(playerWithMostDistricts.get().getOpponentCharacter(), card.getDistrict());
                    return;
                } catch (IllegalArgumentException e) {
                    view.displayPlayerStrategy(this, this + " ne peut pas détruire le quartier " + card.getDistrict().getName() + " du joueur " + playerWithMostDistricts.get() + ", il passe donc à la carte suivante");
                }
            }
        }
    }

    @Override
    protected void useEffectMagician(Magician magician) {
        int numberOfCardsToExchange = this.getHand().size();

        Optional<Opponent> playerWithMostDistricts = this.getOpponents().stream()
                .max(Comparator.comparingInt(Opponent::getHandSize));

        // Case 1: Player has no cards in hand or fewer cards than the player with the most districts
        if (playerWithMostDistricts.isPresent() && numberOfCardsToExchange < playerWithMostDistricts.get().getHandSize()) {
            magician.useEffect(playerWithMostDistricts.get(), null);
            return;
        }

        // Case 2: Player exchanges cards with the deck (cost > 2 gold)
        List<Card> cardsToExchange = this.getHand().stream()
                .filter(card -> card.getDistrict().getCost() > 2)
                .toList();

        if (!cardsToExchange.isEmpty()) {
            magician.useEffect(null, cardsToExchange);
        }
    }

    @Override
    protected void useEffectAssassin(Assassin assassin) {
        // TODO
    }

    /**
     * Il finit sa citadelle s'il peut en un coup, sinon il pose une merveille, sinon il complète les 5
     * couleurs de districtType sinon il joue comme un joueur normal
     */
    protected void useEffectOfTheArchitect() {
        int numberOfCardsNeededToFinishTheGame = 8 - this.getCitadel().size();
        //On regarde s'il peut finir la partie en un coup en vérifiant si la citadelle a plus de 4 cartes, si dans sa main il a au moins 3 cartes
        //On vérifie s'il peut acheter les x districts manquant en choisissant les moins chèrs
        int nbDistrictsCanBeBuild = this.getNbDistrictsCanBeBuild();
        if (this.getCitadel().size() >= 5 && this.getHand().size() >= 3 && getPriceOfNumbersOfCheaperCards(numberOfCardsNeededToFinishTheGame) >= this.getNbGold()) {
            this.playCards(nbDistrictsCanBeBuild);
        } else {
            //on vérifie s'il y a une merveille dans sa main, si oui et qu'il peut la jouer alors il le fait
            Optional<Card> prestigeCard = this.getHand().stream().filter(card -> card.getDistrict().getDistrictType() == DistrictType.PRESTIGE).findFirst();
            if (prestigeCard.isPresent() && canPlayCard(prestigeCard.get())) {
                playCard(prestigeCard.get());
            } else if (!this.hasFiveDifferentDistrictTypes()) {
                //il cherche à avoir les 5 districts de couleur dans sa citadelle sinon
                architectTryToCompleteFiveDistrictTypes();
            } else {
                //Il joue comme un joueur normal
                this.playCards(nbDistrictsCanBeBuild);
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
                    playCard(cardChosen);
                    numberOfCards++;
                    cardNeeded.remove(cardChosen);
                }
            }
            i++;
        }
        //S'il n'y a aucune carte disponible ou bien qu'il ne peut en poser aucune alors il joue comme un joueur normal
        if (numberOfCards == 0) {
            this.playCards(this.getNbDistrictsCanBeBuild());
        }
    }


    /**
     * Returns the target of the assassin chosen by using the strength of characters or randomly if no "interesting" character has been found
     *
     * @return the target of the assassin
     */
    protected Character choseAssassinTarget() {
        List<Role> roleInterestingToKill = new ArrayList<>(List.of(Role.ARCHITECT, Role.MERCHANT, Role.KING));
        Collections.shuffle(roleInterestingToKill);
        Character target = null;
        List<Character> charactersList = this.getAvailableCharacters();
        for (Role role : roleInterestingToKill) {
            for (Character character : charactersList) {
                if (character.getRole() == role) {
                    target = character;
                    break;
                }
            }
        }
        if (target == null) {
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
    public boolean wantToUseManufactureEffect() {
        // if the bot has less than 2 cards in hand, it will use the manufacture effect to get more cards
        return this.getNbGold() > 3 && (this.getHand().size() < 2 || this.isLate());
    }

    /**
     * Determines if the bot is late or not
     *
     * @return true if the bot has less cards in his citadel than the average of the opponents
     */
    public boolean isLate() {
        return averageOpponentCitadelSize() > this.getCitadel().size();
    }

    public double averageOpponentCitadelSize() {
        OptionalDouble average = this.getOpponents().stream().mapToInt(opponent -> opponent.getCitadel().size()).average();
        if (average.isEmpty()) {
            return 0;
        }
        return average.getAsDouble();
    }

    /**
     * Le voleur choisit en priorité le marchand et l'architecte et s'il n'est pas disponible dans les opponents il prend un personnage en aléatoire
     * @param thief the thief
     */
    @Override
    protected void useEffectThief(Thief thief) {
        Optional<Opponent> victim = this.getOpponents().stream().filter(player -> player.getOpponentCharacter().getRole() != Role.ASSASSIN &&
                !player.getOpponentCharacter().isDead() && (player.getOpponentCharacter().getRole() == Role.ARCHITECT || player.getOpponentCharacter().getRole() == Role.MERCHANT)).findFirst();
        if (victim.isEmpty()) {
            victim = this.getOpponents().stream().filter(player -> player.getOpponentCharacter().getRole() != Role.ASSASSIN &&
                    !player.getOpponentCharacter().isDead()).findFirst();
        }
        victim.ifPresent(player -> {
            thief.useEffect(player.getOpponentCharacter());
            view.displayPlayerUseThiefEffect(this);
        });
    }

    @Override
    public String toString() {
        return "Le bot malin " + this.id;
    }

}
