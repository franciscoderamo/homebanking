package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.List;


public interface CardService {

    List<CardDTO> getAllCardsDTO();

    void saveCard(Card card);

    boolean cardExistsByNumber(String number);

    boolean cardExistsByCvv(short cvv);

    Card findById(long id);

    CardDTO getCard(Long id);

    List<CardDTO> findByClientEmail (String email);
}