package com.hotel.hotel.modules.user.dtos;

import com.hotel.hotel.modules.user.model.Role;

public record UserEditDTO(String name, String login, String password, String phoneNumber, Role role) {}
