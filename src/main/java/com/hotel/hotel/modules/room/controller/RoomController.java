package com.hotel.hotel.modules.room.controller;

import com.hotel.hotel.modules.room.dtos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import com.hotel.hotel.infra.dtos.MessageResponse;
import com.hotel.hotel.modules.room.service.RoomService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/room")
public class RoomController {
    
    @Autowired
    private RoomService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity create(@RequestPart("room_data") @Valid RoomSaveDTO data, @RequestPart("images") List<MultipartFile> files, UriComponentsBuilder uriBuilder) {
        log.info("Received a request to create a room");
        var room = service.create(data, files);
        var uri = uriBuilder.path("/room/{id}").buildAndExpand(room.getId()).toUri();
        log.info("Room was successfully created");
        return ResponseEntity.created(uri).body(new RoomDetailsDTO(room));
    }

    @GetMapping
    public ResponseEntity<Page<RoomListDTO>> list(RoomFilters filters, Pageable pagination) {
        log.info("Received request to list rooms");
        var rooms = service.list(filters, pagination);
        log.info("Responding request to list rooms");
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity edit(@RequestPart("room_data") @Valid RoomEditDTO data, @RequestPart(value = "images", required = false) List<MultipartFile> files,  @PathVariable Long id) {
        log.info("Received request to edit room with ID: {}", id);
        var room = service.edit(data, id, files);
        log.info("Room with ID: {} was successfully edited", id);
        return ResponseEntity.ok(new RoomDetailsDTO(room));
    }

    @GetMapping("/{id}")
    public ResponseEntity getDetails(@PathVariable Long id) {
        log.info("Received request to retrieve rooms details with ID: {}", id);
        var room = service.getDetails(id);
        log.info("Room with ID: {} successfully retrieved");
        return ResponseEntity.ok(room);
    }


    @PatchMapping("/finishCleaning/{id}")
    public ResponseEntity finishCleaning(@PathVariable Long id) {
        log.info("Received request to clean room with ID: {}", id);
        service.finishCleaning(id);
        log.info("Room with ID: {} was successfully cleaned", id);
        return ResponseEntity.ok(new MessageResponse("Room cleaning finished succesfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Received request to delete room with ID: {}", id);
        service.delete(id);
        log.info("Room with ID: {} was successfully deleted", id);
        return ResponseEntity.ok(new MessageResponse("Room deleted succesfully"));
    }
}
