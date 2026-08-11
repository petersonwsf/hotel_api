package com.hotel.hotel.modules.user.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hotel.hotel.infra.exceptions.AccessResourceDeniedException;
import com.hotel.hotel.infra.exceptions.ResourceAlreadyExists;
import com.hotel.hotel.infra.exceptions.ResourceNotFoundException;
import com.hotel.hotel.infra.security.TokenService;
import com.hotel.hotel.modules.audit.AuditService;
import com.hotel.hotel.modules.audit.Auditable;
import com.hotel.hotel.modules.user.dtos.UserEditDTO;
import com.hotel.hotel.modules.user.dtos.UserFilters;
import com.hotel.hotel.modules.user.dtos.UserLoginDTO;
import com.hotel.hotel.modules.user.dtos.UserSaveDTO;
import com.hotel.hotel.modules.user.model.Role;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuditService auditService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserRepository repository;

    @Auditable(action = "USER_RESGISTER", resourceType = "USER")
    public User register(UserSaveDTO userData) {
        User newUser = new User(userData);
        verifyLoginExists(userData.login(), null);
        verifyPhoneNumberExists(userData.phoneNumber(), null);
        User user = repository.save(newUser);
        log.info("User {} successfully created", userData.login());
        return user;
    }

    @Auditable(action = "USER_LOGIN", resourceType = "USER")
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

    public User findByLoginUser(String login) {
        var user = repository.findByUsername(login);
        return user; 
    }

    public Page<User> list(Pageable pagination, UserFilters filters) {
        log.info("Listando usuários da página {} com tamanho {}", pagination.getPageNumber(), pagination.getPageSize());
        Specification<User> specification = (root, query, criteriaBuilder) -> null;

        specification = specification
                .and(UserSpecification.filterByName(filters.name()))
                .and(UserSpecification.filterByPhoneNumber(filters.phoneNumber()))
                .and(UserSpecification.filterByRole(filters.role()))
                .and(UserSpecification.filterByLogin(filters.login()));

        log.info("Retornando usuáriosda página {} com tamanho {}", pagination.getPageNumber(), pagination.getPageSize());
        return repository.findAll(specification, pagination);
    }

    @Auditable(action = "USER_DELETE", resourceType = "USER")
    @Transactional
    public void delete(Long id) {
        log.info("Deletando usuário com Id: {}", id);
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        resourceBelongsUser(user);
        user.delete();
        log.info("Usuário com ID: {} deletado com sucesso", id);
    }

    @Transactional
    public User edit(UserEditDTO userData, long id) {
        log.info("Editando usuário com ID: {}", id);
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        String beforeUpdate = null;
        try {
            beforeUpdate = objectMapper.writeValueAsString(user);
        } catch (JsonProcessingException e) {
            log.warn("Não foi possível tratar o JSON para auditoria " + e);
        }
        resourceBelongsUser(user);
        verifyLoginExists(userData.login(), id);
        verifyPhoneNumberExists(userData.phoneNumber(), id);
        user.edit(userData);
        auditService.recordUpdate("UPDATE_USER", "USER", String.valueOf(id), beforeUpdate, user);
        log.info("Usuário com ID: {} editado com sucesso", id);
        return user;
    }

    private void resourceBelongsUser(User user) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        User userAuthenticated = (User) auth.getPrincipal();
        if ((!userAuthenticated.getId().equals(user.getId())) && userAuthenticated.getRole() != Role.ADMIN) throw new AccessResourceDeniedException("Você não tem permissão para este recurso");
    }

    private void verifyLoginExists(String login, Long id) {
        if (login == null) return;

        Boolean loginAlreadyUsed = (id != null)
                ? repository.existsByLoginAndIdNot(login, id)
                : repository.existsByLogin(login);

        if (loginAlreadyUsed) throw new ResourceAlreadyExists("Login já está em uso, tente outro");
    }


    private void verifyPhoneNumberExists(String phoneNumber, Long id) {
        if (phoneNumber == null) return;
        Boolean phoneNumberAlreadyUsed = (id != null)
                ? repository.existsByPhoneNumberAndIdNot(phoneNumber, id)
                : repository.existsByPhoneNumber(phoneNumber);

        if (phoneNumberAlreadyUsed) throw new ResourceAlreadyExists("Login já está em uso, tente outro");
    }
}
