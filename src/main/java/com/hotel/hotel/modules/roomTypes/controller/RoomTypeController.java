package com.hotel.hotel.modules.roomTypes.controller;

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
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeDetailsDTO;
import com.hotel.hotel.modules.roomTypes.dtos.RoomTypeSaveDTO;
import com.hotel.hotel.modules.roomTypes.service.RoomTypeService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/roomtypes")
public class RoomTypeController {

    @Autowired
    private RoomTypeService service;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid RoomTypeSaveDTO data, UriComponentsBuilder uriBuilder) {
        log.info("Received a request to register a new room type");
        var roomType = service.create(data);
        var uri = uriBuilder.path("/roomType/{id}").buildAndExpand(roomType.getId()).toUri();
        log.info("New room type successfully created");
        return ResponseEntity.created(uri).body(new RoomTypeDetailsDTO(roomType));
    }

    @GetMapping
    public ResponseEntity<Page<RoomTypeDetailsDTO>> list(Pageable pagination) {
        log.info("Received a request to list room types");
        var pages = service.list(pagination).map(RoomTypeDetailsDTO::new);
        log.info("Returning a list room types");
        return ResponseEntity.ok(pages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomTypeDetailsDTO> getDetails(@PathVariable Long id) {
        log.info("Received a request to retrieve room types details with ID: {}", id);
        var roomType = service.getDetails(id);
        log.info("Returning room types details with ID: {}", id);
        return ResponseEntity.ok(new RoomTypeDetailsDTO(roomType));
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody RoomTypeSaveDTO data, @PathVariable Long id) {
        log.info("Received a request to edit room types with ID: {}", id);
        var roomType = service.edit(data, id);
        log.info("Room types with ID: {} successfully edited", id);
        return ResponseEntity.ok(new RoomTypeDetailsDTO(roomType));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Received a request to delete room types with ID: {}", id);
        service.deleteById(id);
        log.info("Room types with ID: {} successfully deleted", id);
        return ResponseEntity.ok(new MessageResponse("Room type deleted successfully"));
    }


}
