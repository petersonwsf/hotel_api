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

import com.hotel.hotel.domain.client.ClientRepository;
import com.hotel.hotel.domain.reservation.Reservation;
import com.hotel.hotel.domain.reservation.ReservationDetailsDTO;
import com.hotel.hotel.domain.reservation.ReservationEditDTO;
import com.hotel.hotel.domain.reservation.ReservationRepository;
import com.hotel.hotel.domain.reservation.ReservationSaveDTO;
import com.hotel.hotel.domain.reservation.Status;
import com.hotel.hotel.domain.room.RoomRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationRepository repository;
    
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomRepository roomRepository;
    
    @PostMapping
    @Transactional
    public ResponseEntity create(@RequestBody @Valid ReservationSaveDTO data, UriComponentsBuilder uriBuilder) {
        
        var client = clientRepository.getReferenceById(data.clientId());
        var room = roomRepository.getReferenceById(data.roomId());
        
        var reservation = new Reservation(data.checkInDate(), data.checkOutDate(), data.dailyRate(), data.discountAmount(), data.serviceFee(), data.status(), data.source());
        reservation.assignClient(client);
        reservation.assignRoom(room);
    
        var days = data.checkOutDate().toEpochDay() - data.checkInDate().toEpochDay();
        var totalAmount = (data.dailyRate().multiply(java.math.BigDecimal.valueOf(days))).subtract(data.discountAmount()).add(data.serviceFee());
        reservation.setTotalAmount(totalAmount);

        var reservation_response = repository.save(reservation);

        var uri = uriBuilder.path("/reservation/{id}").buildAndExpand(reservation.getId()).toUri();

        return ResponseEntity.created(uri).body(new ReservationDetailsDTO(reservation_response));
    }

    @GetMapping
    public ResponseEntity<Page<ReservationDetailsDTO>> list(Pageable pagination) {
        var reservations = repository.findByStatusNot(pagination, Status.CANCELED).map(ReservationDetailsDTO::new);
        return ResponseEntity.ok(reservations);
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity edit(@RequestBody @Valid ReservationEditDTO data, @PathVariable Long id) {
        
        Reservation reservation = repository.getReferenceById(id);
        if (data.roomId() != null) {
            var room = roomRepository.getReferenceById(data.roomId());
            reservation.changeRoom(room);
        }
        reservation.edit(data);

        var total_amount = (reservation.getDailyRate().multiply(java.math.BigDecimal.valueOf(reservation.getCheckOutDate().toEpochDay() - reservation.getCheckInDate().toEpochDay())))
            .subtract(reservation.getDiscountAmount())
            .add(reservation.getServiceFee());

        reservation.setTotalAmount(total_amount);

        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity delete(@PathVariable Long id) {
        var reservation = repository.getReferenceById(id);
        reservation.changeStatus(Status.CANCELED);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        var reservation = repository.getReferenceById(id);
        return ResponseEntity.ok(new ReservationDetailsDTO(reservation));
    }
}
