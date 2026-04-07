package com.hotel.hotel.modules.client.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.client.dtos.ClientEditDTO;
import com.hotel.hotel.modules.client.dtos.ClientFilter;
import com.hotel.hotel.modules.client.dtos.ClientSaveDTO;
import com.hotel.hotel.modules.client.model.Client;
import com.hotel.hotel.modules.client.repository.ClientRepository;
import com.hotel.hotel.modules.client.repository.specs.ClientSpecification;
import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;
import com.hotel.hotel.modules.user.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClientService {

    @Autowired
    private ClientRepository repository;

    @Autowired
    private UserRepository userRepository;

    public Client create(ClientSaveDTO data) throws ResourceAlreadyExists {
        log.info("Starting process to create client {}", data.name());
        var emailAlreadyExists = repository.findByEmail(data.email());
        if (emailAlreadyExists.isPresent()) {
            log.warn("The email sent by {} already been used", data.name());
            throw new ResourceAlreadyExists("Email already used");
        }
    
        var phoneAlreadyExists = repository.findByContactInformation_PhoneNumber(data.contactInformation().phoneNumber());
        if (phoneAlreadyExists.isPresent()) {
            log.warn("The phone number sent by {} already been used", data.name());
            throw new ResourceAlreadyExists("Phone number already used");
        }
        var pinAlreadyExists = repository.findByPin(data.pin());
        if (pinAlreadyExists.isPresent()) {
            log.warn("The pin sent by {} already been used", data.name());
            throw new ResourceAlreadyExists("Pin already used");
        }

        User newUser = new User(data.name(), data.email(), data.password(), Role.CLIENT);

        User user = userRepository.save(newUser);
        log.info("The client user {} was successfully created", data.name());
        
        Client client = new Client(data, user);
        
        Client newClient = repository.save(client);
        log.info("The client {} was successfully created", data.name());

        return newClient;
    }

    public Page<Client> list(ClientFilter filter, Pageable pagination) {
        log.info("Starting process to list clients");

        Specification<Client> filters = (root, query, callBack) -> null;
        
        filters = filters
            .and(ClientSpecification.nameLike(filter.name()))
            .and(ClientSpecification.pinEqual(filter.pin()))
            .and(ClientSpecification.emailEqual(filter.email()))
            .and(ClientSpecification.phoneNumberEqual(filter.phoneNumber()))
            .and(ClientSpecification.isDeleted(filter.deleted()));

        log.info("Returning the clients list");
        return repository.findAll(filters, pagination);
    }

    public Client edit(ClientEditDTO data, Long id) {
        log.info("Starting process to edit client eith ID: {}", data.id());
        Client client = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " does not exists"));
        
        if (data.email() != null) {
            var emailAlreadyExists = repository.findByEmail(data.email());
            if (emailAlreadyExists.isPresent()) {
                log.warn("Email sent by user with ID {} already been used", data.id());
                throw new ResourceAlreadyExists("Email already exists");
            };
        }

        if (data.contactInformation() != null && data.contactInformation().phoneNumber() != null) {
            var phoneAlreadyExists = repository.findByContactInformation_PhoneNumber(data.contactInformation().phoneNumber());
            if (phoneAlreadyExists.isPresent()) {
                log.warn("Phone number sent by user with ID: {} already been used", data.id());
                throw new ResourceAlreadyExists("Phone number already exists");
            }
        }

        client.edit(data);
        log.info("Client with ID: {} successfully edited");
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
