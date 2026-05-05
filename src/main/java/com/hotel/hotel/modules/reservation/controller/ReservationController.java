package com.hotel.hotel.modules.reservation.controller;

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
import com.hotel.hotel.modules.reservation.dtos.ReservationDetailsDTO;
import com.hotel.hotel.modules.reservation.dtos.ReservationEditDTO;
import com.hotel.hotel.modules.reservation.dtos.ReservationFilters;
import com.hotel.hotel.modules.reservation.dtos.ReservationSaveDTO;
import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.service.ReservationService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService service;
    
    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid ReservationSaveDTO data, UriComponentsBuilder uriBuilder) {
        log.info("Received a request to create a reservation for client ID: {} ", data.clientId());
        Reservation reservation = service.create(data);
        var uri = uriBuilder.path("/reservation/{id}").buildAndExpand(reservation.getId()).toUri();
        log.info("Reservation for client ID: {} was successfully created", data.clientId());
        return ResponseEntity.created(uri).body(new ReservationDetailsDTO(reservation));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationDetailsDTO>> list(ReservationFilters filters, Pageable pagination) {
        log.info("Received a request to list reservations");
        var reservations = service.list(filters, pagination).map(ReservationDetailsDTO::new);
        log.info("Responding request to list reservations");
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid ReservationEditDTO data, @PathVariable Long id) {
        log.info("Received a request to edit a reservation ID: {} ", id);
        var reservation = service.edit(data, id);
        log.info("Reservation with ID: {} was successfully edited", id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Received a request to delete a reservation ID: {} ", id);
        service.cancel(id);
        log.info("Reservation with ID: {} was successfully deleted", id);
        return ResponseEntity.ok(new MessageResponse("Reservation cancelled succesfully"));
    }

    @PatchMapping("/confirm/{id}")
    @Transactional
    public ResponseEntity confirm(@PathVariable Long id) {
        log.info("Received a request to confirm a reservation ID: {} ", id);
        service.confirm(id);
        log.info("Reservation with ID: {} was successfully confirmed", id);
        return ResponseEntity.ok(new MessageResponse("Reservation confirmed succesfully"));
    }

    @PatchMapping("/checkIn/{id}")
    @Transactional
    public ResponseEntity checkIn(@PathVariable Long id) {
        log.info("Received a request to check-in a reservation ID: {} ", id);
        service.checkIn(id);
        log.info("Reservation with ID: {} was successfully check-in", id);
        return ResponseEntity.ok(new MessageResponse("Check-In successful"));
    }

    @PatchMapping("/checkOut/{id}")
    @Transactional
    public ResponseEntity checkOut(@PathVariable Long id) {
        log.info("Received a request to check-out a reservation ID: {} ", id);
        service.checkOut(id);
        log.info("Reservation with ID: {} was successfully check-out", id);
        return ResponseEntity.ok(new MessageResponse("Check-Out successful"));
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        log.info("Received a request to retrieve a reservation ID: {} ", id);
        var reservation = service.getById(id);
        log.info("Reservation with ID: {} was successfully retrieved", id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<Page<ReservationDetailsDTO>> listReservationsByClient(@PathVariable Long clientId, Pageable pagination) {
        log.info("Received a request to list reservation of client with ID: {} ", clientId);
        var reservations = service.listReservationsByClient(clientId, pagination).map(ReservationDetailsDTO::new);
        log.info("Responding a request to list reservations of client with ID: {}", clientId);
        return ResponseEntity.ok(reservations);
    }
}
