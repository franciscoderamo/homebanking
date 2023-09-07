package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    // Find By Client Id
    List<Card> findByClientId(long id);
    // Find By Client Email
    List<Card> findByClientEmail(String email);
    // Card number exists
    boolean existsByNumber(String number);
    // CVV number exists
    boolean existsByCvv(int cvv);
}