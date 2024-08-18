package io.github.qndev.springbasicauth.service;

import io.github.qndev.springbasicauth.authentication.BasicAuthenticationToken;
import io.github.qndev.springbasicauth.entity.Users;
import io.github.qndev.springbasicauth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BasicAuthenticationService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public BasicAuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        Users userDetails = this.userRepository.findByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Bad request authentication");
        }

        String providedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(providedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad request authentication");
        }

        // if (!"ROLE_USER".equals(userDetails.getRole())) {
        //     throw new AccessDeniedException("Access denied");
        // }

        // Set default SYSTEM role to pass the checking in BasicAuthenticationProvider
        // Will be checked role and permission in RoleBaseAuthorizationManager
        SimpleGrantedAuthority role = new SimpleGrantedAuthority("SYSTEM");
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(role);

        return BasicAuthenticationToken.authenticated(userDetails.getId(),
                authentication.getCredentials(),
                grantedAuthorities);
    }

}
