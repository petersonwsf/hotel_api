package com.hotel.hotel.modules.client.dtos;

import java.time.LocalDate;

import com.hotel.hotel.modules.client.model.Client;
import com.hotel.hotel.modules.contactInformation.dtos.ContactInformationDTO;

public record ClientListDTO(Long id, String name, String email, LocalDate dateOfBirth, ContactInformationDTO contactInformation) {
    public ClientListDTO(Client client) {
        this(client.getId(), client.getName(), client.getEmail(), client.getDateOfBirth(), new ContactInformationDTO(client.getContactInformation()));
    }
}
