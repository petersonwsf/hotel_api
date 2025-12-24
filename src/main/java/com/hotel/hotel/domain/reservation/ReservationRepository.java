package com.hotel.hotel.domain.reservation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Page<Reservation> findByStatusNot(Pageable pageable, Status status);
    List<Reservation> findByClientId(Long clientId);

    @Query("SELECT r FROM Reservation r WHERE r.room.id = :roomId AND r.status <> 'CANCELLED' AND ((r.checkInDate >= :startDate AND r.checkInDate <= :endDate) OR (r.checkOutDate >= :startDate AND r.checkOutDate <= :endDate))")
    List<Reservation> findByCheckInDateBetween(
        @Param("startDate") LocalDate startDate, 
        @Param("endDate") LocalDate endDate, 
        @Param("roomId") Long roomId);
    
}
