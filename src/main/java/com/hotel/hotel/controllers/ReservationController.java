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
import com.hotel.hotel.domain.reservation.Reservation;
import com.hotel.hotel.domain.reservation.ReservationDetailsDTO;
import com.hotel.hotel.domain.reservation.ReservationEditDTO;
import com.hotel.hotel.domain.reservation.ReservationSaveDTO;
import com.hotel.hotel.services.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService service;
    
    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid ReservationSaveDTO data, UriComponentsBuilder uriBuilder) {
        Reservation reservation = service.create(data);
        var uri = uriBuilder.path("/reservation/{id}").buildAndExpand(reservation.getId()).toUri();
        return ResponseEntity.created(uri).body(new ReservationDetailsDTO(reservation));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationDetailsDTO>> list(Pageable pagination) {
        var reservations = service.list(pagination).map(ReservationDetailsDTO::new);
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid ReservationEditDTO data, @PathVariable Long id) {
        var reservation = service.edit(data, id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
       service.deleteById(id);
        return ResponseEntity.ok(new MessageResponse("Reservation cancelled succesfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        var reservation = service.getById(id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }
}
