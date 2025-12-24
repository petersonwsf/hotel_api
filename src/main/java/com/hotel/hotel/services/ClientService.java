package com.hotel.hotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hotel.hotel.domain.client.Client;
import com.hotel.hotel.domain.client.ClientEditDTO;
import com.hotel.hotel.domain.client.ClientRepository;
import com.hotel.hotel.domain.client.ClientSaveDTO;
import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    public Client create(ClientSaveDTO data) throws ResourceAlreadyExists {

        var emailAlreadyExists = repository.findByEmail(data.email());
        if (emailAlreadyExists.isPresent()) {
            throw new ResourceAlreadyExists("Email already used");
        }
        var phoneAlreadyExists = repository.findByContactInformation_PhoneNumber(data.contactInformation().phoneNumber());
        if (phoneAlreadyExists.isPresent()) {
            throw new ResourceAlreadyExists("Phone number already used");
        }
        var pinAlreadyExists = repository.findByPin(data.pin());
        if (pinAlreadyExists.isPresent()) {
            throw new ResourceAlreadyExists("Pin already used");
        }
        
        Client client = new Client(data);
        
        Client newClient = repository.save(client);

        return newClient;
    }

    public Page<Client> list(Pageable pagination) {
        return repository.findAllByDeletedFalse(pagination);
    }

    public Client edit(ClientEditDTO data, Long id) {
        Client client = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " does not exists"));
        
        if (data.email() != null) {
            var emailAlreadyExists = repository.findByEmail(data.email());
            if (emailAlreadyExists.isPresent()) throw new ResourceAlreadyExists("Email already exists");
        }

        if (data.contactInformation() != null && data.contactInformation().phoneNumber() != null) {
            var phoneAlreadyExists = repository.findByContactInformation_PhoneNumber(data.contactInformation().phoneNumber());
            if (phoneAlreadyExists.isPresent()) throw new ResourceAlreadyExists("Phone number already exists");
        }

        client.edit(data);

        return client;
    }

    public void deleteById(Long id) {
        Client client = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " does not exists"));
        client.delete();
    }

    public Client getById(Long id) {
        Client client = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " does not exists"));   
        return client;
    }
    
}
