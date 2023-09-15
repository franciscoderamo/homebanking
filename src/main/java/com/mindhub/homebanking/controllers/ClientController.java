package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mindhub.homebanking.models.Client.passwordValidator;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO(){
        return clientService.getClientsDTO();
    }
    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return clientService.getCurrentClient(authentication.getName());
    }
    @PostMapping("/clients")
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

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        try{
            Client client = new Client(firstName, lastName, email, passwordValidator(password));
            client.setPassword(passwordEncoder.encode(client.getPassword()));
            clientService.saveClient(client);
            // Create the first customer account and associate it
            Account account = new Account(createRandomNumberAccount(1, 99999999), LocalDate.now(),0.0);
            account.setClient(client);
            accountService.saveAccount(account);
        }catch (IllegalArgumentException exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    public String createRandomNumberAccount(int min, int max) {
        int number = (int) ((Math.random() * (max - min)) + min);
        return "VIN" + number;
    }
}