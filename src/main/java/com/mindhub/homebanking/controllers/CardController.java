package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    private CardService cardService;

    @Autowired
    private ClientService clientService;

    @RequestMapping("/cards")
    public List<CardDTO> getAllCardsDTO(){
        return cardService.getAllCardsDTO();
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentClientCards(Authentication authentication){
        String clientEmail = authentication.getName(); //Get customer email
        return cardService.findByClientEmail(clientEmail);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        //Authentication object verification
        if (authentication == null || authentication.getName() == null) {
            return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }
        String clientEmail = authentication.getName(); //Get customer email
        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Missing data: card type or card color is empty", HttpStatus.FORBIDDEN);
        }
        Client authenticatedClient = clientService.findByEmail(authentication.getName());
        if (authenticatedClient != null) {

            Set<Card> clientCards = authenticatedClient.getCards();
            short cvv;
            String cardNumber;
            // Limit the cardholder to 14 characters
            String cardHolder = (authenticatedClient.getLastName() + " " + authenticatedClient.getFirstName());

            // Check cardType and cardColor limits
            int cardsOfTypeAndColor = (int) clientCards.stream().filter(card -> card.getType() == cardType && card.getColor() == cardColor).count();
            if (cardsOfTypeAndColor == 0) {
                do {
                    cvv = generateRandomCvv();
                    cardNumber = generateRandomCardNumber();
                    Card newCard = new Card(cardHolder, cardType, cardColor, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5));
                    //newCard.setClient(clientRepository.findByEmail(authentication.getName()));
                    cardService.saveCard(newCard);
                    //Associate client with card and save in ClientRepository
                    authenticatedClient.addCard(newCard);
                    clientService.saveClient(authenticatedClient);
                    return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
                } while (cardService.cardExistsByNumber(cardNumber) || cardService.cardExistsByCvv(cvv)); // Check if the generated card number or CVV already exist in the card repository
            } else {
                return new ResponseEntity<>("You can only have a single card of the same type and color", HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>("Invalid authenticated client", HttpStatus.UNAUTHORIZED);
        }
    }
    // Generate random CVV
    private short generateRandomCvv(){
        return (short) (Math.random() * 999);
    }
    // Generate random card number
    private String generateRandomCardNumber() {
        StringBuilder number = new StringBuilder();
        DecimalFormat format = new DecimalFormat("0000");
        for (int i = 0; i < 4; i++) {
            number.append(format.format((int)(Math.random() * 9999)));
            if (i != 3) {
                number.append("-");
            }
        }
        return number.toString();
    }
}