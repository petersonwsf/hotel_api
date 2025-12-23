package com.hotel.hotel.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.hotel.hotel.domain.client.ClientDetailsDTO;
import com.hotel.hotel.domain.client.ClientEditDTO;
import com.hotel.hotel.domain.client.ClientListDTO;
import com.hotel.hotel.domain.client.ClientSaveDTO;
import com.hotel.hotel.domain.dtos.MessageResponse;
import com.hotel.hotel.services.ClientService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid ClientSaveDTO data, UriComponentsBuilder uriBuilder) {
        var client = service.create(data);
        var uri = uriBuilder.path("/client/{id}").buildAndExpand(client.id()).toUri();
        return ResponseEntity.created(uri).body(client);
    }

    @GetMapping
    public ResponseEntity<Page<ClientListDTO>> list(Pageable pagination) {
        var clients = service.list(pagination);
        return ResponseEntity.ok(clients);
    }
    
    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid ClientEditDTO data, @PathVariable Long id) {
        ClientDetailsDTO client = service.edit(data, id);
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Client deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity getClientById(@PathVariable Long id) {
        var client = service.getById(id);
        return ResponseEntity.ok(client);
    }
}