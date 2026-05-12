package com.hotel.hotel.modules.client.model;

import java.time.LocalDate;
import com.hotel.hotel.modules.client.dtos.ClientEditDTO;
import com.hotel.hotel.modules.client.dtos.ClientSaveDTO;
import com.hotel.hotel.modules.contactInformation.model.ContactInformation;
import com.hotel.hotel.modules.user.model.User;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Client")
@Table(name = "client")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pin;
    private String email;
    private LocalDate dateOfBirth;
    private Boolean deleted;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private ContactInformation contactInformation;

    public Client(ClientSaveDTO data, User user) {
        this.deleted = false;
        this.name = data.name();
        this.pin = data.pin();
        this.email = data.email();
        this.dateOfBirth = data.dateOfBirth();
        this.contactInformation = new ContactInformation(data.contactInformation());
        this.user = user;
    }

    public void edit(ClientEditDTO data) {
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.email() != null) {
            this.email = data.email();
        }
        if (data.dateOfBirth() != null) {
            this.dateOfBirth = data.dateOfBirth();
        }
        if (data.contactInformation() != null) {
            this.contactInformation.edit(data.contactInformation());
        }
    }

    public void delete() {
        this.deleted = true;
    }
}
