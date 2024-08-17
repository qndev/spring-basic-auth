package io.github.qndev.springbasicauth.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Slf4j
public class CustomBasicAuthenticationFilter extends BasicAuthenticationFilter {

    public CustomBasicAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationEntryPoint authenticationEntryPoint) {
        super(authenticationManager, authenticationEntryPoint);
    }

}
