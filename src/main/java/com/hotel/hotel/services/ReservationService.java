package com.hotel.hotel.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.hotel.hotel.domain.reservation.Reservation;
import com.hotel.hotel.domain.reservation.ReservationEditDTO;
import com.hotel.hotel.domain.reservation.ReservationFilters;
import com.hotel.hotel.domain.reservation.ReservationRepository;
import com.hotel.hotel.domain.reservation.ReservationSaveDTO;
import com.hotel.hotel.domain.reservation.ReservationSpecification;
import com.hotel.hotel.domain.reservation.Status;
import com.hotel.hotel.domain.room.StatusRoom;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.infra.exceptions.RoomNotAvailable;

@Service
public class ReservationService {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private ClientService clientService;

    @Autowired
    private ReservationRepository repository;

    public Reservation create(ReservationSaveDTO data) {
        var client = clientService.getById(data.clientId());
        var room = roomService.getDetails(data.roomId());

        var reservationDateBetween = repository.findByCheckInDateBetween(data.checkInDate(), data.checkOutDate(), data.roomId());
        for (Reservation reservation : reservationDateBetween) {
            if (reservation.getStatus() != Status.CANCELED) {
                throw new RoomNotAvailable("Room is not available for the selected dates");
            }
        }

        var days = data.checkOutDate().toEpochDay() - data.checkInDate().toEpochDay();

        var totalAmount = data.dailyRate().multiply(BigDecimal.valueOf(days)).add(data.serviceFee()).subtract(data.discountAmount());

        Reservation reservation = new Reservation(data);

        reservation.assignClient(client);
        reservation.assignRoom(room);
        reservation.setTotalAmount(totalAmount);
    
        Reservation newReservation = repository.save(reservation);

        return newReservation;
    }

    public Page<Reservation> list(ReservationFilters filters, Pageable pagination) {

        Specification<Reservation> filter = (root, query, criteriaBuilder) -> null;

        filter = filter.and(ReservationSpecification.checkDateBetween(filters.checkInDate(), filters.checkOutDate()))
                .and(ReservationSpecification.clientIdEqual(filters.client()))
                .and(ReservationSpecification.roomIdEqual(filters.room()))
                .and(ReservationSpecification.sourceEqual(filters.source()))
                .and(ReservationSpecification.statusEqual(filters.status()));

        return repository.findAll(filter, pagination);
    }

    public Reservation getById(Long id) {
        var reservation = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        return reservation;
    }

    public Reservation edit(ReservationEditDTO data, Long id) {

        var reservation = getById(id);

        if (data.checkInDate() != null && data.checkOutDate() != null && data.roomId() != null) {
            List<Reservation> reservationDateBetween = repository.findByCheckInDateBetween(data.checkInDate(), data.checkOutDate(), data.roomId());
            if (!reservationDateBetween.isEmpty()) {
                throw new RoomNotAvailable("Room is not available for the selected dates");
            }
            var newRoom = roomService.getDetails(data.roomId());
            reservation.assignRoom(newRoom);
        } else if (data.checkInDate() != null && data.checkOutDate() != null) {
            List<Reservation> reservationDateBetween = repository.findByCheckInDateBetween(data.checkInDate(), data.checkOutDate(), reservation.getRoom().getId());
            if (!reservationDateBetween.isEmpty()) {
                for (Reservation r : reservationDateBetween) {
                    if (!r.getId().equals(reservation.getId())) {
                        throw new RoomNotAvailable("Room is not available for the selected dates");
                    }
                }
            }
        }

        reservation.edit(data);

        var days = reservation.getCheckOutDate().toEpochDay() - reservation.getCheckInDate().toEpochDay();
        var totalAmount = reservation.getDailyRate().multiply(BigDecimal.valueOf(days)).add(reservation.getServiceFee()).subtract(reservation.getDiscountAmount());

        reservation.setTotalAmount(totalAmount);

        return reservation;
    }

    public void cancel(Long id) {
        var reservation = getById(id);
        reservation.changeStatus(Status.CANCELED);
    }

    public void confirm(Long id) {
        var reservation = getById(id);
        reservation.changeStatus(Status.CONFIRMED);
    }

    public void checkIn(Long id) {
        var reservation = getById(id);
        var room = reservation.getRoom();
        room.changeStatus(StatusRoom.OCCUPIED);
        reservation.changeStatus(Status.CHECKED_IN);
    }

    public void checkOut(Long id) {
        var reservation = getById(id);
        var room = reservation.getRoom();
        room.changeStatus(StatusRoom.CLEANING);
        reservation.changeStatus(Status.CHECKED_OUT);
    }

}
