package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientService clientService;


    @RequestMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.getClients();
    }
    @RequestMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {
        return clientService.getClient(id);
    }

    @RequestMapping("/clients/current")
    public ClientDTO getCurrentClient(Authentication authentication){
        return clientService.getCurrentClient(authentication);
    }
    @RequestMapping(path = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        return clientService.register(firstName,lastName,email,password);
    }
    public String createRandomNumberAccount(int min, int max) {
        return clientService.createRandomNumberAccount(min,max);
    }
}