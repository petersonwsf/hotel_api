package com.hotel.hotel.modules.reservation.repository.specs;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.model.Status;

public class ReservationSpecification {

    public static Specification<Reservation> checkDateBetween(LocalDate checkIn, LocalDate checkOut) {
        return (root, query, criteriaBuilder) -> {
            if (checkIn == null && checkOut == null) return null;

            if (checkIn != null && checkOut == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("checkOutDate"), checkIn);
            }
            if (checkIn == null && checkOut != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("checkInDate"), checkOut);
            }

            return criteriaBuilder.between(root.get("checkInDate"), checkIn, checkOut);
        };
    }

    public static Specification<Reservation> statusEqual(Status status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Reservation> userIdEqual(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) return null;
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Reservation> roomIdEqual(Long roomId) {
        return (root, query, criteriaBuilder) -> {
            if (roomId == null) return null;
            return criteriaBuilder.equal(root.get("room").get("id"), roomId);
        };
    }

    
    
}
