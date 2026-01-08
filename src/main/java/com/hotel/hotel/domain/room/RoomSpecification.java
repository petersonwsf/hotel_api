package com.hotel.hotel.domain.room;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

public class RoomSpecification {
    
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

    public static Specification<Room> roomTypeIdEqual(Long roomTypeId) {
        return (root, query, criteriaBuilder) -> {
            if (roomTypeId == null) return null;
            return criteriaBuilder.equal(root.get("roomType").get("id"), roomTypeId);
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
}
