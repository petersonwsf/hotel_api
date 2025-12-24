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

import com.hotel.hotel.domain.dtos.MessageResponse;
import com.hotel.hotel.domain.room.Room;
import com.hotel.hotel.domain.room.RoomDetailsDTO;
import com.hotel.hotel.domain.room.RoomEditDTO;
import com.hotel.hotel.domain.room.RoomSaveDTO;
import com.hotel.hotel.services.RoomService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/room")
public class RoomController {
    
    @Autowired
    private RoomService service;

    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid RoomSaveDTO data, UriComponentsBuilder uriBuilder) {
        Room room = service.create(data);
        var uri = uriBuilder.path("/room/{id}").buildAndExpand(room.getId()).toUri();
        return ResponseEntity.created(uri).body(new RoomDetailsDTO(room));
    }

    @GetMapping
    public ResponseEntity<Page<RoomDetailsDTO>> list(Pageable pagination) {
        var rooms = service.list(pagination).map(RoomDetailsDTO::new);
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid RoomEditDTO data, @PathVariable Long id) {
        var room = service.edit(data, id);
        return ResponseEntity.ok(new RoomDetailsDTO(room));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(new MessageResponse("Room deleted succesfully"));
    }
}
