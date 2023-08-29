package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByClientId(long id);
    List<Card> findByClientEmail(String email);
    boolean existsByNumber(String number);
    boolean existsByCvv(int cvv);
}
