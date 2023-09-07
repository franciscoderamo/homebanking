package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private CardService cardService;

    @RequestMapping("/cards")
    public List<CardDTO> getAllCards(){
        return cardService.getAllCards();
    }

    @RequestMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable Long id) {
        return cardService.getCard(id);
    }

    @RequestMapping("/clients/current/cards")
    public List<CardDTO> getCurrentClientCards(Authentication authentication){
        return cardService.getCurrentClientCards(authentication);
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType, @RequestParam CardColor cardColor, Authentication authentication){
        return cardService.createCard(cardType, cardColor, authentication);
    }
    public short generateRandomCvv(){
        return cardService.generateRandomCvv();
    }
    // Generate random card number
    public String generateRandomCardNumber() {
        return cardService.generateRandomCardNumber();
    }
}