package io.github.qndev.springbasicauth.authorization;

import io.github.qndev.springbasicauth.service.RoleBaseAuthorizationService;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Component
public class RoleBaseAuthorizationManager implements AuthorizationManager<HttpServletRequest> {

    private final RoleBaseAuthorizationService roleBaseAuthorizationService;

    public RoleBaseAuthorizationManager(RoleBaseAuthorizationService roleBaseAuthorizationService) {
        this.roleBaseAuthorizationService = roleBaseAuthorizationService;
    }

    @Override
    public void verify(Supplier<Authentication> authentication, HttpServletRequest request) {
        AuthorizationManager.super.verify(authentication, request);
    }

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, HttpServletRequest request) {
        return this.roleBaseAuthorizationService.authorize(authentication.get(), request);
    }

}
