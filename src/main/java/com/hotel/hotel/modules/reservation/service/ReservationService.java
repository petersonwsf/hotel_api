package com.hotel.hotel.modules.reservation.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.infra.exceptions.AccessResourceDeniedException;
import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;
import com.hotel.hotel.modules.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.infra.exceptions.RoomNotAvailable;
import com.hotel.hotel.modules.reservation.dtos.ReservationEditDTO;
import com.hotel.hotel.modules.reservation.dtos.ReservationFilters;
import com.hotel.hotel.modules.reservation.dtos.ReservationSaveDTO;
import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.model.Status;
import com.hotel.hotel.modules.reservation.repository.ReservationRepository;
import com.hotel.hotel.modules.reservation.repository.specs.ReservationSpecification;
import com.hotel.hotel.modules.room.model.StatusRoom;
import com.hotel.hotel.modules.room.service.RoomService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReservationService {
    
    @Autowired
    private RoomService roomService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationRepository repository;

    public Reservation create(ReservationSaveDTO data) {
        log.info("Starting process to create reservation for client with ID: {}", data.userId());
        var user = userService.findById(data.userId());
        var room = roomService.getDetails(data.roomId());
        validateReservationDate(data.checkInDate(), data.checkOutDate(), data.roomId(), null);
        BigDecimal totalAmount = calculateTotalAmount(data.checkInDate(), data.checkOutDate(), data.dailyRate(), data.serviceFee(), data.discountAmount());
        Reservation reservation = new Reservation(data);
        reservation.assignClient(user);
        reservation.assignRoom(room);
        reservation.setTotalAmount(totalAmount);
        Reservation newReservation = repository.save(reservation);
        log.info("The reservation was successfully created in the database");
        return newReservation;
    }

    public Page<Reservation> list(ReservationFilters filters, Pageable pagination) {
        log.info("Starting process to list reservations");

        Specification<Reservation> filter = (root, query, criteriaBuilder) -> null;

        filter = filter.and(ReservationSpecification.checkDateBetween(filters.checkInDate(), filters.checkOutDate()))
                .and(ReservationSpecification.userIdEqual(filters.user()))
                .and(ReservationSpecification.roomIdEqual(filters.room()))
                .and(ReservationSpecification.sourceEqual(filters.source()))
                .and(ReservationSpecification.statusEqual(filters.status()));

        log.info("Returning the reservations list");
        return repository.findAll(filter, pagination);
    }

    public Reservation getById(Long id) {
        log.info("Finding reservation with ID: {}", id);
        var reservation = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
        userHasPermission(reservation);
        return reservation;
    }

    public Reservation edit(ReservationEditDTO data, Long id) {
        log.info("Starting process to edit reservation with ID: {}", id);
        var reservation = getById(id);
        userHasPermission(reservation);
        validateReservationDate(data.checkInDate(), data.checkOutDate(), reservation.getRoom().getId(), reservation.getId());

        if (!data.roomId().equals(reservation.getRoom().getId())) {
            var newRoom = roomService.getDetails(data.roomId());
            reservation.assignRoom(newRoom);
        }
        reservation.edit(data);

        log.info("Recalculating values");
        BigDecimal totalAmount = calculateTotalAmount(
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getDailyRate(),
                reservation.getServiceFee(),
                reservation.getDiscountAmount());
        reservation.setTotalAmount(totalAmount);
        log.info("Reservation with ID: {} was successfully edited", id);
        return reservation;
    }

    @Transactional
    public void cancel(Long id) {
        log.info("Canceling reservation with ID: {}" , id);
        var reservation = getById(id);
        userHasPermission(reservation);
        reservation.changeStatus(Status.CANCELED);
        log.info("Reservation with ID: {} successfully canceled" , id);
    }

    @Transactional
    public void confirm(Long id) {
        log.info("Confirming reservation with ID: {}" , id);
        var reservation = getById(id);
        userHasPermission(reservation);
        reservation.changeStatus(Status.CONFIRMED);
        log.info("Reservation with ID: {} successfully confirmed" , id);
    }

    public void checkIn(Long id) {
        log.info("Checking in to the reservation with ID: {}", id);
        var reservation = getById(id);
        var room = reservation.getRoom();
        userHasPermission(reservation);
        room.changeStatus(StatusRoom.OCCUPIED);
        reservation.changeStatus(Status.CHECKED_IN);
    }

    public void checkOut(Long id) {
        log.info("Checking out to the reservation with ID: {}", id);
        var reservation = getById(id);
        var room = reservation.getRoom();
        userHasPermission(reservation);
        room.changeStatus(StatusRoom.CLEANING);
        reservation.changeStatus(Status.CHECKED_OUT);
    }

    public Page<Reservation> listReservationsByUser(Long clientId, Pageable pagination) {
        log.info("Listing reservations of the client with ID: {}", clientId);
        Page<Reservation> reservations = repository.findByUserId(clientId, pagination);
        if (!reservations.isEmpty()) {
            userHasPermission(reservations.getContent().get(0));
        }
        return reservations;
    }

    private void userHasPermission(Reservation reservation) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        if ((reservation.getUser().getId() != user.getId()) && user.getRole() == Role.CLIENT) throw new AccessResourceDeniedException("Você não tem acesso a este recurso");
    }

    private BigDecimal calculateTotalAmount(LocalDate checkIn, LocalDate checkOut, BigDecimal dailyRate, BigDecimal serviceFee, BigDecimal discountAmount) {
        var days = checkOut.toEpochDay() - checkIn.toEpochDay();
        return dailyRate.multiply(BigDecimal.valueOf(days)).add(serviceFee).subtract(discountAmount);
    }

    private void validateReservationDate(LocalDate checkIn, LocalDate checkOut, Long roomId, Long id) {
        if (repository.existsOverlappingById(roomId, checkIn, checkOut, id)) throw new RoomNotAvailable("Quarto não disponível na data indicada");
    }
}
