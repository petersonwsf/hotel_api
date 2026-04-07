package com.hotel.hotel.modules.contactInformation.model;

import com.hotel.hotel.modules.contactInformation.dtos.ContactInformationDTO;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ContactInformation {
    private String phoneNumber; 
    private String street; 
    private String neighborhood; 
    private String number; 
    private String city; 
    private String state; 
    private String complement; 
    private String postalCode;

    public ContactInformation(ContactInformationDTO data) {
        this.phoneNumber = data.phoneNumber();
        this.street = data.street();
        this.neighborhood = data.neighborhood();
        this.state = data.state();
        this.city = data.city();
        this.complement = data.complement();
        this.postalCode = data.postalCode();
        this.number = data.number();
    }

    public void edit(ContactInformationDTO data) {
        if (data.phoneNumber() != null) {
            this.phoneNumber = data.phoneNumber();
        }
        if (data.street() != null) {
            this.street = data.street();
        }
        if (data.neighborhood() != null) {
            this.neighborhood = data.neighborhood();
        }
        if (data.state() != null) {
            this.state = data.state();
        }
        if (data.city() != null) {
            this.city = data.city();
        }
        if (data.complement() != null) {
            this.complement = data.complement();
        }
        if (data.postalCode() != null) {
            this.postalCode = data.postalCode();
        }
        if (data.number() != null) {
            this.number = data.number();
        }
    }
}
