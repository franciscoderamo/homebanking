package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

import java.util.List;
import java.util.Set;


public interface CardService {

    List<Card> findAllCard();
    List<CardDTO> getAllCardsDTO();

    void saveCard(Card card);

    boolean cardExistsByNumber(String number);

    Card findCardByNumber(String number);

    boolean cardExistsByCvv(short cvv);

    Card findById(long id);

    CardDTO getCard(Long id);

    List<CardDTO> findByClientEmail (String email);

    void deleteCard(String number);

}