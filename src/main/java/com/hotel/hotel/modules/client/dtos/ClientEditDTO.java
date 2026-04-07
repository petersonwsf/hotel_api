package com.hotel.hotel.modules.client.dtos;

import java.time.LocalDate;

import com.hotel.hotel.modules.contactInformation.dtos.ContactInformationDTO;

import jakarta.validation.constraints.NotNull;

public record ClientEditDTO(
    
    @NotNull
    Long id, 
    String name, 
    String email, 
    LocalDate dateOfBirth, 
    ContactInformationDTO contactInformation
) {}
