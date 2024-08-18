package io.github.qndev.springbasicauth.authentication;

import io.github.qndev.springbasicauth.service.BasicAuthenticationService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class BasicAuthenticationProvider implements AuthenticationProvider {

    private final BasicAuthenticationService basicAuthenticationService;

    public BasicAuthenticationProvider(BasicAuthenticationService basicAuthenticationService) {
        this.basicAuthenticationService = basicAuthenticationService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return this.basicAuthenticationService.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
