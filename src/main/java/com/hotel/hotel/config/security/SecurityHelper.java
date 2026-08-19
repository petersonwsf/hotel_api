package com.hotel.hotel.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hotel.hotel.config.exceptions.AccessResourceDeniedException;
import com.hotel.hotel.config.exceptions.ResourceNotFoundException;
import com.hotel.hotel.modules.client.model.Client;
import com.hotel.hotel.modules.reservation.model.Reservation;
import com.hotel.hotel.modules.reservation.repository.ReservationRepository;
import com.hotel.hotel.modules.user.model.Role;
import com.hotel.hotel.modules.user.model.User;

@Component("securityHelper")
public class SecurityHelper {

    @Autowired
    private ReservationRepository reservationRepository;
    
    public boolean hasClientPermission(Long id) {
        User user = getAuthenticatedUser();
        if (!id.equals(user.getId()) && user.getRole() == Role.CLIENT) throw new AccessResourceDeniedException("Você não tem acesso a este recurso");
        return true;
    }

    public boolean hasUserPermission(Long id) {
        User userAuthenticated = getAuthenticatedUser();
        if ((!userAuthenticated.getId().equals(id)) && userAuthenticated.getRole() != Role.ADMIN) throw new AccessResourceDeniedException("Você não tem permissão para este recurso");
        return true;
    }

    public boolean hasClientReservationPermission(Long id) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return true;
        }
        if (!(auth.getPrincipal() instanceof User user)) {
            return true;
        }
        var reservation = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reserva não encontrada"));
        if ((reservation.getUser().getId() != user.getId()) && user.getRole() == Role.CLIENT) throw new AccessResourceDeniedException("Você não tem acesso a este recurso");
        return true;
    }

    private User getAuthenticatedUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof User user)) {
            throw new AccessResourceDeniedException("Usuário não autenticado");
        }
        return user;
    }
}
