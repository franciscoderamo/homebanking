package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    @RequestMapping("/cards")
    public List<CardDTO> getAllCards(){
        return cardRepository.findAll().stream().map(CardDTO::new).collect(Collectors.toList());
    }
    @Override
    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }
    @Override
    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentClientCards(Authentication authentication){
        String clientEmail = authentication.getName(); //Get customer email
        return cardRepository.findByClientEmail(clientEmail).stream().map(CardDTO::new).collect(Collectors.toList());
    }
    @Override
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
        Client authenticatedClient = clientRepository.findByEmail(authentication.getName()).orElse(null);
        if (authenticatedClient != null) {

            Set<Card> clientCards = authenticatedClient.getCards();
            // Check cardType and cardColor limits
            int cardsOfTypeAndColor = (int) clientCards.stream().filter(card -> card.getType() == cardType && card.getColor() == cardColor).count();
            short cvv;
            String cardNumber;
            String cardHolder = authenticatedClient.getLastName() + " " + authenticatedClient.getFirstName();
            //Get only the first 14 characters of the string
            if (cardHolder.length() > 14) {
                cardHolder = cardHolder.substring(0, 14);
            }

            if (cardsOfTypeAndColor == 0) {
                do {
                    cvv = generateRandomCvv();
                    cardNumber = generateRandomCardNumber();
                    Card newCard = new Card(cardHolder, cardType, cardColor, cardNumber, cvv, LocalDate.now(), LocalDate.now().plusYears(5));
                    //newCard.setClient(clientRepository.findByEmail(authentication.getName()));
                    cardRepository.save(newCard);
                    //Associate client with card and save in ClientRepository
                    authenticatedClient.addCard(newCard);
                    clientRepository.save(authenticatedClient);
                    return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);
                } while (cardRepository.existsByNumber(cardNumber) || cardRepository.existsByCvv(cvv)); // Check if the generated card number or CVV already exist in the card repository
            } else {
                return new ResponseEntity<>("You can only have a single card of the same type and color", HttpStatus.FORBIDDEN);
            }

        } else {
            return new ResponseEntity<>("Invalid authenticated client", HttpStatus.UNAUTHORIZED);
        }
    }
    // Generate random CVV
    @Override
    public short generateRandomCvv(){
        return (short) (Math.random() * 999);
    }
    // Generate random card number
    @Override
    public String generateRandomCardNumber() {
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