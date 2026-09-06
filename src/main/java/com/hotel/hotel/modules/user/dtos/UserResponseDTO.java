package com.hotel.hotel.modules.user.dtos;

import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;

public record UserResponseDTO(Long id, String name, String login, String phoneNumber, Role role, String imageKey) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName(), user.getUsername(), user.getPhoneNumber(), user.getRole(), user.getProfilePicture());
    }
}
