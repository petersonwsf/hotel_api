package com.hotel.hotel.modules.user.dtos;

import java.util.List;

import com.hotel.hotel.modules.user.model.Role;

public record UserFilters(String name, String login, List<Role> role, String phoneNumber, Boolean deleted) {
}
