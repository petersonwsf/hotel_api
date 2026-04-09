package com.hotel.hotel.modules.room.controller;

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

import com.hotel.hotel.infra.dtos.MessageResponse;
import com.hotel.hotel.modules.room.dtos.RoomDetailsDTO;
import com.hotel.hotel.modules.room.dtos.RoomEditDTO;
import com.hotel.hotel.modules.room.dtos.RoomFilters;
import com.hotel.hotel.modules.room.dtos.RoomSaveDTO;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.service.RoomService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {
    
    @Autowired
    private RoomService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid RoomSaveDTO data, UriComponentsBuilder uriBuilder) {
        log.info("Received a request to create a room");
        Room room = service.create(data);
        var uri = uriBuilder.path("/room/{id}").buildAndExpand(room.getId()).toUri();
        log.info("Room was successfully created");
        return ResponseEntity.created(uri).body(new RoomDetailsDTO(room));
    }

    @GetMapping
    public ResponseEntity<Page<RoomDetailsDTO>> list(RoomFilters filters, Pageable pagination) {
        log.info("Received request to list rooms");
        var rooms = service.list(filters, pagination).map(RoomDetailsDTO::new);
        log.info("Responding request to list rooms");
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid RoomEditDTO data, @PathVariable Long id) {
        log.info("Received request to edit room with ID: {}", id);
        var room = service.edit(data, id);
        log.info("Room with ID: {} was successfully edited", id);
        return ResponseEntity.ok(new RoomDetailsDTO(room));
    }

    @PatchMapping("/finishCleaning/{id}")
    @Transactional
    public ResponseEntity finishCleaning(@PathVariable Long id) {
        log.info("Received request to clean room with ID: {}", id);
        service.finishCleaning(id);
        log.info("Room with ID: {} was successfully cleaned", id);
        return ResponseEntity.ok(new MessageResponse("Room cleaning finished succesfully"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Received request to delete room with ID: {}", id);
        service.delete(id);
        log.info("Room with ID: {} was successfully deleted", id);
        return ResponseEntity.ok(new MessageResponse("Room deleted succesfully"));
    }
}
