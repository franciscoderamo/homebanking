package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;


public interface CardService {

    public List<CardDTO> getAllCards();

    public CardDTO getCard(Long id);

    public List<CardDTO> getCurrentClientCards(Authentication authentication);

    public ResponseEntity<Object> createCard(CardType cardType, CardColor cardColor, Authentication authentication);
    public short generateRandomCvv();
    public String generateRandomCardNumber();
}
