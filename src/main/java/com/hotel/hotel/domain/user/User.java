package com.hotel.hotel.domain.user;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

@Table(name = "app_user")
@Entity(name = "User")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User implements UserDetails{
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String login;
    private String password;
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "papel")
    private Role role;

    public User(UserSaveDTO data) {
        var bcrypt = new BCryptPasswordEncoder();
        this.name = data.name();
        this.login = data.login();
        this.password = bcrypt.encode(data.password());
        this.role = data.role();
        this.deleted = false;
    }

    public void edit(UserSaveDTO data) {
        var bcrypt = new BCryptPasswordEncoder();
        if (data.name() != null) {
            this.name = data.name();
            this.login = data.login();
            this.password = bcrypt.encode(data.password());
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
    public String getUsername() {
        return login;
    }
}
