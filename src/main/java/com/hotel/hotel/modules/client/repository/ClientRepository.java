package com.hotel.hotel.modules.client.repository;

import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.hotel.hotel.modules.client.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
    Page<Client> findAllByDeletedFalse(Pageable pagination);
    Optional<Client> findById(Long id);
    Optional<Client> findByEmail(String email);
    Optional<Client> findByContactInformation_PhoneNumber(String phoneNumber);
    Optional<Client> findByPin(String pin);
    Optional<Client> findByUserId(Long userId);
}
