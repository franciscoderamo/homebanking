package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import java.util.List;
public interface ClientService {

    public List<ClientDTO> getClients();

    public ClientDTO getClient(Long id);

    public ClientDTO getCurrentClient(Authentication authentication);

    public ResponseEntity<Object> register(String firstName, String lastName, String email, String password);
    public String createRandomNumberAccount(int min, int max);
}
