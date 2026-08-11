package com.hotel.hotel.modules.client.dtos;

import java.time.LocalDate;

import com.hotel.hotel.modules.contactInformation.dtos.ContactInformationDTO;

public record ClientEditDTO(
    String name, 
    String email,
    String pin,
    LocalDate dateOfBirth, 
    ContactInformationDTO contactInformation
) {}
