package com.hotel.hotel.modules.client.service;

import com.hotel.hotel.infra.exceptions.AccessResourceDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
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

    public Client create(ClientSaveDTO data) {
        log.info("Starting process to create client {}", data.name());

        verifyEmailExists(data.email(), null);
        verifyPhoneNumberExists(data.contactInformation().phoneNumber(), null);
        verifyPinExists(data.pin(), null);

        User newUser = new User(data.name(), data.email(), data.password(), data.contactInformation().phoneNumber(), Role.CLIENT);
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
        Client client = getById(id);
        userHasPermission(client);
        if (data.email() != null) {
            verifyEmailExists(data.email(), id);
        }
        if (data.contactInformation() != null && data.contactInformation().phoneNumber() != null) {
            verifyPhoneNumberExists(data.contactInformation().phoneNumber(), id);
        }
        client.edit(data);
        log.info("Client with ID: {} successfully edited");
        return client;
    }

    public void deleteById(Long id) {
        Client client = getById(id);
        userHasPermission(client);
        client.delete();
    }

    public Client getById(Long id) {
        Client client = repository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with id " + id + " does not exists"));
        userHasPermission(client);
        return client;
    }
    
    public Client getClientByUserId(Long id) {
        Client client = repository.findByUserId(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Client with user id " + id + " does not exists"));
        userHasPermission(client);
        return client;
    }

    private void verifyEmailExists(String email, Long id) {
        Boolean emailExists = (id != null)
                ? repository.existsByEmailAndIdNot(email, id)
                : repository.existsByEmail(email);
        if (emailExists) throw new ResourceAlreadyExists("Email já está sendo utilizado, tente outro");
    }

    private void verifyPinExists(String pin, Long id) {
        Boolean pinExists = (id != null)
                ? repository.existsByPinAndIdNot(pin, id)
                : repository.existsByPin(pin);
        if (pinExists) throw new ResourceAlreadyExists("CPF já está sendo utilizado, tente outro");
    }

    private void verifyPhoneNumberExists(String phoneNumber, Long id) {
        Boolean phoneNumberExists = (id != null)
                ? repository.existsByContactInformation_PhoneNumberAndIdNot(phoneNumber, id)
                : repository.existsByEmail(phoneNumber);
        if (phoneNumberExists) throw new ResourceAlreadyExists("Número de telefone já está sendo utilizado, tente outro");
    }

    private void userHasPermission(Client client) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if ((client.getUser().getId() != user.getId()) && user.getRole() == Role.CLIENT) throw new AccessResourceDeniedException("Você não tem acesso a este recurso");
    }

}
