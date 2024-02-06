package com.seinksansdoozebank.fr.model.player;

import com.seinksansdoozebank.fr.model.cards.Card;
import com.seinksansdoozebank.fr.model.cards.Deck;
import com.seinksansdoozebank.fr.model.cards.DistrictType;
import com.seinksansdoozebank.fr.model.character.abstracts.Character;
import com.seinksansdoozebank.fr.model.character.abstracts.CommonCharacter;
import com.seinksansdoozebank.fr.model.character.roles.Role;
import com.seinksansdoozebank.fr.view.IView;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BotBuilder extends SmartBot {
    public BotBuilder(int nbGold, Deck deck, IView view) {
        super(nbGold, deck, view);
    }

    /**
     * Choose a character from the list of characters
     * If the bot has a lot of gold, it will choose the architect to build
     * If the bot has a green district, it will choose the merchant to get more golds
     * If none of the above, it will choose the king to get more golds
     * If none of the above, it will choose a random character
     *
     * @param characters the list of characters to choose from
     * @return the chosen character
     */
    @Override
    public Character chooseCharacterImpl(List<Character> characters) {
        if (this.getNbGold() >= 5) {
            // Check for the architect
            Optional<Character> architect = characters.stream()
                    .filter(character -> character.getRole().equals(Role.ARCHITECT))
                    .findFirst();
            if (architect.isPresent()) {
                return architect.get();
            }
        }

        // Check for the Merchant with green districts
        Optional<Character> merchantWithGreenDistricts = characters.stream()
                .filter(character -> character.getRole().equals(Role.MERCHANT) &&
                        this.getHand().stream().anyMatch(district -> district.getDistrict().getDistrictType().equals(DistrictType.TRADE_AND_CRAFTS)))
                .findFirst();

        if (merchantWithGreenDistricts.isPresent()) {
            return merchantWithGreenDistricts.get();
        }

        // Check for the King
        Optional<Character> king = characters.stream()
                .filter(character -> character.getRole().equals(Role.KING))
                .findFirst();

        return king.orElseGet(() -> characters.get(random.nextInt(characters.size())));
    }

    /**
     * Keep the card with the highest cost of the same type as the character's target
     * - The first priority is to keep the card type of nobility
     * - The second priority is to keep the card type of trade and crafts
     * - The third priority is to keep the card type of prestige
     * - If none of the above, keep the card with the highest cost
     *
     * @param pickedCards the cards picked
     * @return the card to keep
     */
    @Override
    protected Card keepOneDiscardOthers(List<Card> pickedCards) {
        Optional<Card> kingCard = pickedCards.stream()
                .filter(card -> card.getDistrict().getDistrictType().equals(DistrictType.NOBILITY))
                .findFirst();

        if (kingCard.isPresent()) {
            return kingCard.get();
        }

        Optional<Card> merchantCard = pickedCards.stream()
                .filter(card -> card.getDistrict().getDistrictType().equals(DistrictType.TRADE_AND_CRAFTS))
                .findFirst();

        if (merchantCard.isPresent()) {
            return merchantCard.get();
        }

        Optional<Card> prestigeCard = pickedCards.stream()
                .filter(card -> card.getDistrict().getDistrictType().equals(DistrictType.PRESTIGE))
                .findFirst();

        return prestigeCard.orElseGet(() -> super.keepOneDiscardOthers(pickedCards));
    }


    @Override
    protected Optional<Card> chooseCard() {
        //Gathering districts which are not already built in player's citadel
        List<Card> notAlreadyPlayedCardList = this.getHand().stream().filter(d -> !this.getCitadel().contains(d)).toList();
        Optional<Card> cardToPlay;
        if (this.character instanceof CommonCharacter commonCharacter) {
            DistrictType target = commonCharacter.getTarget();
            cardToPlay = notAlreadyPlayedCardList.stream()
                    .filter(card -> card.getDistrict().getDistrictType().equals(target)) // filter the cards that are the same as the character's target
                    .max(Comparator.comparing(card -> card.getDistrict().getCost())); // choose the most expensive card
        } else {
            cardToPlay = this.getMostExpansiveCard(notAlreadyPlayedCardList);
        }
        if (cardToPlay.isPresent() && this.canPlayCard(cardToPlay.get())) {
            return cardToPlay;
        } else {
            return this.getMostExpansiveCard(notAlreadyPlayedCardList);
        }
    }


    protected Optional<Card> getMostExpansiveCard(List<Card> notAlreadyPlayedCardList) {
        return notAlreadyPlayedCardList.stream().max(Comparator.comparing(card -> card.getDistrict().getCost()));
    }


    @Override
    protected void useEffectOfTheArchitect() {
        int numberOfCardsNeededToFinishTheGame = 8 - this.getCitadel().size();
        //On regarde s'il peut finir la partie en un coup en vérifiant si la citadelle a plus de 4 cartes, si dans sa main il a au moins 3 cartes
        //On vérifie s'il peut acheter les x districts manquant en choisissant les moins chèrs
        int nbDistrictsCanBeBuild = this.getNbDistrictsCanBeBuild();
        if (this.getCitadel().size() >= 5 && this.getHand().size() >= 3 && getPriceOfNumbersOfCheaperCards(numberOfCardsNeededToFinishTheGame) >= this.getNbGold()) {
            this.buyXCardsAndAddThemToCitadel(nbDistrictsCanBeBuild);
        } else {
            //on vérifie s'il y a une merveille dans sa main, si oui et qu'il peut la jouer alors il le fait
            Optional<Card> prestigeCard = this.getHand().stream().filter(card -> card.getDistrict().getDistrictType() == DistrictType.PRESTIGE).findFirst();
            if (prestigeCard.isPresent() && canPlayCard(prestigeCard.get())) {
                buyACardAndAddItToCitadel(prestigeCard.get());
            } else {
                this.buyXCardsAndAddThemToCitadel(nbDistrictsCanBeBuild);
            }
        }
    }
}