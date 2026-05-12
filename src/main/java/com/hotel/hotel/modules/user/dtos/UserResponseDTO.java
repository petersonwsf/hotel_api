package com.hotel.hotel.modules.user.dtos;

import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;

public record UserResponseDTO(String name, String login, String phoneNumber, Role role) {
    public UserResponseDTO(User user) {
        this(user.getName(), user.getUsername(), user.getPhoneNumber(), user.getRole());
    }
}
