package com.hotel.hotel.domain.client;

import org.springframework.data.jpa.domain.Specification;

public class ClientSpecification {
    public static Specification<Client> nameLike(String name) {
        return (root, query, callBack) -> {
            if (name == null || name.isBlank()) return null;
            return callBack.like(callBack.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Client> pinEqual(String pin) {
        return (root, query, callBack) -> {
            if (pin == null || pin.isBlank()) return null;
            return callBack.equal(root.get("pin"), pin);
        };
    }

    public static Specification<Client> emailEqual(String email) {
        return (root, query, callBack) -> {
            if (email == null || email.isBlank()) return null;
            return callBack.equal(root.get("email"), email);
        };
    }

    public static Specification<Client> phoneNumberEqual(String numberPhone) {
        return (root, query, callBack) -> {
            if (numberPhone == null || numberPhone.isBlank()) return null;
            return callBack.equal(root.get("contactInformation").get("phoneNumber"), numberPhone); 
        };
    }

    public static Specification<Client> isDeleted(Boolean deleted) {
        return (root, query, callBack) -> {
            if (deleted == null) return null;
            return callBack.equal(root.get("deleted"), deleted);
        };
    }
}
