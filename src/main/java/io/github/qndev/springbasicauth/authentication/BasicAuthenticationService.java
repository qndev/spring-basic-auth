package io.github.qndev.springbasicauth.authentication;

import io.github.qndev.springbasicauth.entity.User;
import io.github.qndev.springbasicauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        User userDetails = this.userRepository.findByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Bad request authentication");
        }

        String providedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(providedPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Bad request authentication");
        }

        if (!"ROLE_USER".equals(userDetails.getRole())) {
            throw new AccessDeniedException("Access denied");
        }

        SimpleGrantedAuthority role = new SimpleGrantedAuthority(userDetails.getRole());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(role);

        return BasicAuthenticationToken.authenticated(authentication.getPrincipal(),
                authentication.getCredentials(),
                grantedAuthorities);
    }

}
