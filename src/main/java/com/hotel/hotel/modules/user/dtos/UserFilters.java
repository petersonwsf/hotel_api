package com.hotel.hotel.modules.user.dtos;

import com.hotel.hotel.modules.user.model.Role;

public record UserFilters(String name, String login, Role role, String phoneNumber, Boolean deleted) {
}
