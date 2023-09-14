package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> findAllCard() {
        return cardRepository.findByIsActive(true);
    }

    @Override
    public List<CardDTO> getAllCardsDTO(){
        return findAllCard().stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean cardExistsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public Card findCardByNumber(String number) {
        return cardRepository.findByNumber(number).orElse(null);
    }
    @Override
    public boolean cardExistsByCvv(short cvv) {
        return cardRepository.existsByCvv(cvv);
    }
    @Override
    public Card findById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public CardDTO getCard(Long id) {
        return new CardDTO(this.findById(id));
    }

    @Override
    public List<CardDTO> findByClientEmail (String email){
        return cardRepository.findByClientEmail(email).stream().map(CardDTO::new).collect(Collectors.toList());
    }

    @Override
    public void deleteCard(String number) {
        Card card = findCardByNumber(number);
        card.setIsActivate();
        this.saveCard(card);
    }

}