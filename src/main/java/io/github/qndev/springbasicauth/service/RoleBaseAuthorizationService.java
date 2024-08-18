package io.github.qndev.springbasicauth.service;

import io.github.qndev.springbasicauth.entity.UserRoleModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RoleBaseAuthorizationService {

    private static final String ROLE_ADMIN = "Admin";

    private final JdbcTemplate jdbcTemplate;

    public RoleBaseAuthorizationService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<UserRoleModel> findUserRoleByUserId(String userId) {
        String sql = "SELECT ur.id AS id," +
                " ur.user_id AS userId," +
                " ur.role_id AS roleId," +
                " rol.name AS roleName" +
                " FROM user_role ur" +
                " INNER JOIN users urs" +
                " ON  ur.user_id = urs.id " +
                " INNER JOIN roles rol" +
                " ON rol.id = ur.role_id" +
                " WHERE ur.user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            UserRoleModel userRole = new UserRoleModel();
            userRole.setId(rs.getString("id"));
            userRole.setUserId(rs.getString("userId"));
            userRole.setRoleId(rs.getString("roleId"));
            userRole.setRoleName(rs.getString("roleName"));
            return userRole;
        });
    }

    public AuthorizationDecision authorize(Authentication authentication, HttpServletRequest request) {
        boolean isGranted = isGranted(authentication, request);
        return new AuthorizationDecision(isGranted);
    }

    private boolean isGranted(Authentication authentication, HttpServletRequest request) {
        return authentication != null && authentication.isAuthenticated() && isAuthorized(authentication, request);
    }

    private boolean isAuthorized(Authentication authentication, HttpServletRequest request) {
        // String requestURI = request.getRequestURI();
        // if (!requestURI.equals("/spring-basic-auth/authenticate")) {
        //     return false;
        // }

        if (authentication instanceof AnonymousAuthenticationToken) {
            return true;
        }

        String userId = (String) authentication.getPrincipal();
        List<UserRoleModel> userRoles = this.findUserRoleByUserId(userId);

        if (CollectionUtils.isEmpty(userRoles)) {
            return false;
        }

        for (UserRoleModel userRole : userRoles) {
            if (ROLE_ADMIN.equals(userRole.getRoleName())) {
                return true;
            }
        }

        return false;
    }

}
