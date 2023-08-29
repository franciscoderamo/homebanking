package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account,Long> {
    // Find by client id
    List<Account> findByClientId(Long client_id);
    // Find by client email
    List<Account> findByClientEmail(String email);
}
