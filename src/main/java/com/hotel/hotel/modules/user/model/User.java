package com.hotel.hotel.modules.user.model;

import java.util.Collection;
import java.util.List;

import com.hotel.hotel.modules.user.dtos.UserEditDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hotel.hotel.modules.user.dtos.UserSaveDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "app_user")
@Entity(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User implements UserDetails{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String login;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel")
    private Role role;

    @Column(name = "profile_picture")
    private String profilePicture;

    public User(UserSaveDTO data) {
        var bcrypt = new BCryptPasswordEncoder();
        this.name = data.name();
        this.login = data.login();
        this.password = bcrypt.encode(data.password());
        this.phoneNumber = data.phoneNumber();
        this.role = data.role();
        this.deleted = false;
    }

    public User(String name, String login, String password, String phoneNumber, Role role) {
        var bcrypt = new BCryptPasswordEncoder();
        this.name = name;
        this.login = login;
        this.password = bcrypt.encode(password);
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.deleted = false;
    }

    public void edit(UserEditDTO data) {
        var bcrypt = new BCryptPasswordEncoder();
        if (data.name() != null) {
            this.name = data.name();
        }
        if (data.login() != null) {
            this.login = data.login();
        }
        if (data.password() != null) {
            this.password = bcrypt.encode(data.password());
        }
        if (data.phoneNumber() != null) {
            this.phoneNumber = data.phoneNumber();
        }
        if (data.role() != null) {
            this.role = data.role();
        }
    }

    public void delete() {
        this.deleted = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return login;
    }
}
