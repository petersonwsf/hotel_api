package com.hotel.hotel.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hotel.hotel.domain.user.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var token = getToken(request);

        if (token != null) {
            var subject = tokenService.getSubject(token);
            var user = userRepository.findByLogin(subject);
            var authorization = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authorization);
        }

        filterChain.doFilter(request, response);
    }
    
    public String getToken(HttpServletRequest httpServletRequest) {
        var token = httpServletRequest.getHeader("Authorization");
        if (token != null) {
            return token.replace("Bearer ", "").trim();
        }
        return null;
    }
}
