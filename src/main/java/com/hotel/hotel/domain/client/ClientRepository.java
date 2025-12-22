package com.hotel.hotel.domain.client;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Page<Client> findAllByDeletedFalse(Pageable pagination);
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByContactInformation_PhoneNumber(String phoneNumber);
    Optional<Client> findByPin(String pin);
}
