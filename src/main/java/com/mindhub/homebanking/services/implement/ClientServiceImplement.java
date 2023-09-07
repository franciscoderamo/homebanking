package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }
    @Override
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientRepository.findById(id).map(ClientDTO::new).orElse(null);
    }

    @Override
    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return new ClientDTO(Objects.requireNonNull(clientRepository.findByEmail(authentication.getName()).orElse(null)));
    }
    @Override
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            String errorMessage = "Missing data: ";
            List<String> missingFields = new ArrayList<>();

            if (firstName.isEmpty()) {
                missingFields.add("firstName");
            }
            if (lastName.isEmpty()) {
                missingFields.add("lastName");
            }
            if (email.isEmpty()) {
                missingFields.add("email");
            }
            if (password.isEmpty()) {
                missingFields.add("password");
            }

            errorMessage += String.join(", ", missingFields);
            return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);

        }

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client client = new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        // Create the first customer account and associate it
        Account account = new Account(createRandomNumberAccount(1, 99999999), LocalDate.now(),0.0);
        account.setClient(client);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @Override
    public String createRandomNumberAccount(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        return "VIN" + number;
    }
}