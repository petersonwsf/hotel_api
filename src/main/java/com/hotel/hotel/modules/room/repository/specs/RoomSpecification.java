package com.hotel.hotel.modules.room.repository.specs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.hotel.modules.room.model.Category;
import org.springframework.data.jpa.domain.Specification;

import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.room.model.Room;
import com.hotel.hotel.modules.room.model.StatusRoom;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Subquery;

public class RoomSpecification {

    public static Specification<Room> roomAvailableOn(LocalDate checkInDate, LocalDate checkOutDate, Long reservationId) {
         return (root, query, criteriaBuilder) -> {
            if (checkInDate == null || checkOutDate == null) return null;

            query.distinct(true);

            Subquery<Long> subquery = query.subquery(Long.class);
            var reservationRoot = subquery.from(Reservation.class);
            var predicates = new ArrayList<Predicate>();
            predicates.add(criteriaBuilder.equal(reservationRoot.get("room").get("id"), root.get("id")));
            predicates.add(criteriaBuilder.lessThan(reservationRoot.get("checkInDate"), checkOutDate));
            predicates.add(criteriaBuilder.greaterThan(reservationRoot.get("checkOutDate"), checkInDate));
                
            if (reservationId != null) {
                predicates.add(criteriaBuilder.notEqual(reservationRoot.get("id"), reservationId));
            }

            subquery.select(reservationRoot.get("room").get("id"))
                .where(predicates.toArray(new Predicate[0]));

            return criteriaBuilder.not(criteriaBuilder.exists(subquery));
        };
    }
    
    public static Specification<Room> codeEqual(String code) {
        return (root, query, criteriaBuilder) -> {
            if (code == null || code.isBlank()) return null;
            return criteriaBuilder.equal(root.get("code"), code);
        };
    }

    public static Specification<Room> floorIn(List<String> floor) {
        return (root, query, criteriaBuilder) -> {
            if (floor == null || floor.isEmpty()) return null;
            return root.get("floor").in(floor);
        };
    }

    public static Specification<Room> statusIn(List<StatusRoom> status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) return null;
            return root.get("status").in(status);
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

    public static Specification<Room> categoryIn(List<Category> category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isEmpty()) return null;
            return root.get("category").in(category);
        };
    }
}
