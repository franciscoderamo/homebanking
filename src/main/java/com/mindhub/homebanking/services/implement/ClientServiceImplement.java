package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientServiceImplement implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<ClientDTO> getClientsDTO() {
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Override
    public Client findById(long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public ClientDTO getClient(long id) {
        return new ClientDTO(this.findById(id));
    }

    @Override
    public ClientDTO getCurrentClient(String email) {
        return new ClientDTO(this.findByEmail(email));
    }

    @Override
    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
}
