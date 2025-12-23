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
import com.hotel.hotel.domain.roomTypes.RoomType;
import com.hotel.hotel.domain.roomTypes.RoomTypeDetailsDTO;
import com.hotel.hotel.domain.roomTypes.RoomTypeRepository;
import com.hotel.hotel.domain.roomTypes.RoomTypeSaveDTO;
import com.hotel.hotel.services.RoomTypeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/roomtypes")
public class RoomTypeController {

    @Autowired
    private RoomTypeService service;

    @PostMapping
    @Transactional
    public ResponseEntity register(@RequestBody @Valid RoomTypeSaveDTO data, UriComponentsBuilder uriBuilder) {
        var roomType = service.create(data);
        var uri = uriBuilder.path("/roomType/{id}").buildAndExpand(roomType.id()).toUri();
        return ResponseEntity.created(uri).body(roomType);
    }

    @GetMapping
    public ResponseEntity<Page<RoomTypeDetailsDTO>> list(Pageable pagination) {
        var pages = service.list(pagination);
        return ResponseEntity.ok(pages);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody RoomTypeSaveDTO data, @PathVariable Long id) {
        var roomType = service.edit(data, id);
        return ResponseEntity.ok(roomType);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Room type deleted successfully"));
    }


}
