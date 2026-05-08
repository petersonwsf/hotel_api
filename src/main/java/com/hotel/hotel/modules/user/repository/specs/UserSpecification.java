package com.hotel.hotel.modules.user.repository.specs;

import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {
    public static Specification<User> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<User> filterByLogin(String login) {
        return (root, query, criteriaBuilder) -> {
            if (login == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("login")), "%" + login.toLowerCase() + "%");
        };
    }

    public static Specification<User> filterByDeleted(Boolean deleted) {
        return (root, query, criteriaBuilder) -> {
            if (deleted == null) return null;
            return criteriaBuilder.equal(root.get("deleted"), deleted);
        };
    }

    public static Specification<User> filterByPhoneNumber(String phoneNumber) {
        return (root, query, criteriaBuilder) -> {
            if (phoneNumber == null) return null;
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("phoneNumber")), "%" + phoneNumber + "%");
        };
    }

    public static Specification<User> filterByRole(Role role) {
        return (root, query, criteriaBuilder) -> {
            if (role == null) return null;
            return criteriaBuilder.equal(root.get("papel"), role);
        };
    }
}
