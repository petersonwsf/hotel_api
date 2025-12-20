package com.hotel.hotel.domain.client;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.hotel.hotel.domain.contactInformation.ContactInformation;
import com.hotel.hotel.domain.contactInformation.ContactInformationDTO;
import com.hotel.hotel.domain.reservation.Reservation;

import org.junit.jupiter.api.Test;

class ClientTest {

  @Test
  void edit_shouldUpdateNonNullFields_andDelegateToContact() {
    ContactInformation ci = new ContactInformation(
        "(81) 99999-9999","Rua X","Centro","123","Casinhas","PE",null,"55750000"
    );
    Client client = new Client(1L,"Old","12345678901","old@mail.com",
        LocalDate.of(1990,1,1), false, ci, new ArrayList<Reservation>());

    ContactInformationDTO ciDto = new ContactInformationDTO(
        "(81) 98888-7777","Nova Rua","Novo Bairro","777","Casinhas","PE","Casa","55750002"
    );
    ClientEditDTO dto = new ClientEditDTO(
        1L,"New Name","new@mail.com", LocalDate.of(1995,5,10), ciDto
    );

    client.edit(dto);

    assertEquals("New Name", client.getName());
    assertEquals("new@mail.com", client.getEmail());
    assertEquals(LocalDate.of(1995,5,10), client.getDateOfBirth());

    assertEquals("(81) 98888-7777", client.getContactInformation().getPhoneNumber());
    assertEquals("Nova Rua", client.getContactInformation().getStreet());
    assertEquals("55750002", client.getContactInformation().getPostalCode());
  }

  @Test
  void edit_shouldIgnoreNullFields() {
    ContactInformation ci = new ContactInformation(
        "(81) 99999-9999","Rua X","Centro","123","Casinhas","PE",null,"55750000"
    );
    Client client = new Client(1L,"Old","12345678901","old@mail.com",
        LocalDate.of(1990,1,1), false, ci, new ArrayList<Reservation>());

    ClientEditDTO dto = new ClientEditDTO(1L, null, null, null, null);

    client.edit(dto);

    assertEquals("Old", client.getName());
    assertEquals("old@mail.com", client.getEmail());
    assertEquals(LocalDate.of(1990,1,1), client.getDateOfBirth());
    // ContactInformation permanece igual
    assertEquals("Rua X", client.getContactInformation().getStreet());
  }

  @Test
  void delete_shouldSetDeletedTrue() {
    ContactInformation ci = new ContactInformation(
        "(81) 99999-9999","Rua X","Centro","123","Casinhas","PE",null,"55750000"
    );
    Client client = new Client(1L,"Old","12345678901","old@mail.com",
        LocalDate.of(1990,1,1), false, ci, new ArrayList<Reservation>());

    client.delete();

    assertTrue(client.getDeleted());
  }
}
