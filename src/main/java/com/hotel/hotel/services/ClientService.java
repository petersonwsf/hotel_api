package com.hotel.hotel.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hotel.hotel.domain.client.Client;
import com.hotel.hotel.domain.client.ClientEditDTO;
import com.hotel.hotel.domain.client.ClientFilter;
import com.hotel.hotel.domain.client.ClientRepository;
import com.hotel.hotel.domain.client.ClientSaveDTO;
import com.hotel.hotel.domain.client.ClientSpecification;
import com.hotel.hotel.domain.user.Role;
import com.hotel.hotel.domain.user.User;
import com.hotel.hotel.domain.user.UserRepository;
import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;

@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private UserRepository userRepository;

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

        User newUser = new User(data.name(), data.email(), data.password(), Role.CLIENT);

        User user = userRepository.save(newUser);
        
        Client client = new Client(data, user);
        
        Client newClient = repository.save(client);

        return newClient;
    }

    public Page<Client> list(ClientFilter filter, Pageable pagination) {

        Specification<Client> filters = (root, query, callBack) -> null;
        
        filters = filters
            .and(ClientSpecification.nameLike(filter.name()))
            .and(ClientSpecification.pinEqual(filter.pin()))
            .and(ClientSpecification.emailEqual(filter.email()))
            .and(ClientSpecification.phoneNumberEqual(filter.phoneNumber()))
            .and(ClientSpecification.isDeleted(filter.deleted()));

        return repository.findAll(filters, pagination);
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
    
    public Client getClientByUserId(Long id) {
        Client client = repository.findByUserId(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with user id " + id + " does not exists"));
        return client;
    }
}
