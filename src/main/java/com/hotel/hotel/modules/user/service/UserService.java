package com.hotel.hotel.modules.user.service;

import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.infra.security.TokenService;
import com.hotel.hotel.modules.user.dtos.UserEditDTO;
import com.hotel.hotel.modules.user.dtos.UserFilters;
import com.hotel.hotel.modules.user.dtos.UserLoginDTO;
import com.hotel.hotel.modules.user.dtos.UserSaveDTO;
import com.hotel.hotel.modules.user.model.User;
import com.hotel.hotel.modules.user.repository.UserRepository;
import com.hotel.hotel.modules.user.repository.specs.UserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository repository;

    public String register(UserSaveDTO userData) {
        User user = new User(userData);
        repository.save(user);
        log.info("User {} successfully created", userData.login());
        return tokenService.createToken(user);
    }

    public String login(UserLoginDTO credentials) {
        var token = new UsernamePasswordAuthenticationToken(credentials.login(), credentials.password());
        log.info("Authentication token generated");
        var authentication = manager.authenticate(token);
        log.info("Successfully authentication");
        return tokenService.createToken((User) authentication.getPrincipal());
    }

    public User findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    public Page<User> list(Pageable pagination, UserFilters filters) {
        Specification<User> specification = (root, query, criteriaBuilder) -> null;

        specification = specification
                .and(UserSpecification.filterByName(filters.name()))
                .and(UserSpecification.filterByPhoneNumber(filters.phoneNumber()))
                .and(UserSpecification.filterByRole(filters.role()))
                .and(UserSpecification.filterByLogin(filters.login()));

        return repository.findAll(specification, pagination);
    }

    public void delete(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        user.delete();
    }

    public User edit(UserEditDTO userData, long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        if (userData.login() != null) {
            UserDetails loginAlreadyUsed = repository.findByLogin(userData.login());
            if (loginAlreadyUsed != null) {
                throw new ResourceAlreadyExists("Login já está em uso no momento");
            }
        }
        user.edit(userData);
        return user;
    }
}
