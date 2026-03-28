package com.hotel.hotel.domain.client;

import java.time.LocalDate;

import com.hotel.hotel.domain.contactInformation.ContactInformationDTO;

public record ClientDetailsDTO(Long id, String name, String pin, String email, LocalDate dateOfBirth, ContactInformationDTO contactInformation, Long userId) {
    public ClientDetailsDTO(Client client) {
        this(client.getId(), client.getName(), client.getPin(), client.getEmail(), client.getDateOfBirth(), new ContactInformationDTO(client.getContactInformation()), client.getUser().getId());
    }
}
