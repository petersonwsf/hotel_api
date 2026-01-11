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
import com.hotel.hotel.domain.reservation.ReservationFilters;
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
    public ResponseEntity<Page<ReservationDetailsDTO>> list(ReservationFilters filters, Pageable pagination) {
        var reservations = service.list(filters, pagination).map(ReservationDetailsDTO::new);
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
       service.cancel(id);
        return ResponseEntity.ok(new MessageResponse("Reservation cancelled succesfully"));
    }

    @PatchMapping("/confirm/{id}")
    @Transactional
    public ResponseEntity confirm(@PathVariable Long id) {
        service.confirm(id);
        return ResponseEntity.ok(new MessageResponse("Reservation confirmed succesfully"));
    }

    @PatchMapping("/checkIn/{id}")
    @Transactional
    public ResponseEntity checkIn(@PathVariable Long id) {
        service.checkIn(id);
        return ResponseEntity.ok(new MessageResponse("Check-In successful"));
    }

    @PatchMapping("/checkOut/{id}")
    @Transactional
    public ResponseEntity checkOut(@PathVariable Long id) {
        service.checkOut(id);
        return ResponseEntity.ok(new MessageResponse("Check-Out successful"));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        var reservation = service.getById(id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }
}
