package com.hotel.hotel.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import com.hotel.hotel.modules.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    UserDetails findByLogin(String login);
    Boolean existsByLoginAndIdNot(String login, Long id);
    Boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);
    Boolean existsByLogin(String login);
    Boolean existsByPhoneNumber(String phoneNumber);
    @Query("SELECT u FROM User u WHERE u.login = :login")
    User findByUsername(String login);
}
