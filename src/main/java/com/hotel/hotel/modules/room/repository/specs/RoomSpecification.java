package com.hotel.hotel.modules.room.repository.specs;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.hotel.hotel.modules.room.model.Category;
import org.springframework.data.jpa.domain.Specification;

import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;

import jakarta.persistence.criteria.Subquery;

public class RoomSpecification {

    public static Specification<Room> roomAvailableOn(LocalDate checkInDate, LocalDate checkOutDate) {
         return (root, query, criteriaBuilder) -> {
            if (checkInDate == null || checkOutDate == null) return null;

            query.distinct(true);

            Subquery<Long> subquery = query.subquery(Long.class);
            var reservationRoot = subquery.from(Reservation.class);
            subquery.select(reservationRoot.get("room").get("id"))
            .where(
                criteriaBuilder.equal(reservationRoot.get("room").get("id"), root.get("id")),
                criteriaBuilder.lessThan(reservationRoot.get("checkInDate"), checkOutDate),
                criteriaBuilder.greaterThan(reservationRoot.get("checkOutDate"), checkInDate)
            );

            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
        };
    }
    
    public static Specification<Room> codeEqual(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) return null;
            return criteriaBuilder.equal(root.get("code"), code);
        };
    }

    public static Specification<Room> floorEqual(String floor) {
        return (root, query, criteriaBuilder) -> {
            if (floor == null || floor.isBlank()) return null;
            return criteriaBuilder.equal(root.get("floor"), floor);
        };
    }

    public static Specification<Room> statusEqual(StatusRoom status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) return null;
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Room> activeEqual(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) return null;
            return criteriaBuilder.equal(root.get("active"), active);
        };
    }

    public static Specification<Room> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }


    public static Specification<Room> capacityGreaterThan(Integer capacity) {
        return (root, query, criteriaBuilder) -> {
            if (capacity == null) return null;
            return criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), capacity);
        };
    }

    public static Specification<Room> categoryEquals(Category category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null) return null;
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }
}
