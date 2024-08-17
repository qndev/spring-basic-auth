package io.github.qndev.springbasicauth.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class BasicAuthenticationProvider implements AuthenticationProvider {

    private BasicAuthenticationService basicAuthenticationService;

    public BasicAuthenticationProvider() {
    }

    public void setBasicAuthenticationService(BasicAuthenticationService basicAuthenticationService) {
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
