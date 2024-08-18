package io.github.qndev.springbasicauth.controller;

import io.github.qndev.springbasicauth.entity.Roles;
import io.github.qndev.springbasicauth.entity.UserRole;
import io.github.qndev.springbasicauth.entity.Users;
import io.github.qndev.springbasicauth.repository.RoleRepository;
import io.github.qndev.springbasicauth.repository.UserRepository;
import io.github.qndev.springbasicauth.repository.UserRoleRepository;
import io.github.qndev.springbasicauth.response.BaseResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class BasicAuthenticationController {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserRoleRepository userRoleRepository;

    private final PasswordEncoder passwordEncoder;

    public BasicAuthenticationController(UserRepository userRepository,
                                         RoleRepository roleRepository,
                                         UserRoleRepository userRoleRepository,
                                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This endpoint is used to generate a user with the Admin role.
     * This endpoint is no need authentication.
     *
     * @param userRequest Users
     * @return BaseResponse
     */
    @PostMapping("/generate-user")
    public BaseResponse generateUser(@RequestBody Users userRequest) {

        Users user = new Users();
        user.setId(UUID.randomUUID().toString());
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        Roles role = new Roles();
        role.setId(UUID.randomUUID().toString());
        role.setName("Admin");
        role.setDescription("Admin role");

        UserRole userRole = new UserRole();
        userRole.setId(UUID.randomUUID().toString());
        userRole.setUserId(user.getId());
        userRole.setRoleId(role.getId());

        userRepository.save(user);
        roleRepository.save(role);
        userRoleRepository.save(userRole);

        return BaseResponse.success("User generated", user);
    }

    /**
     * This endpoint is used to check if the user is authenticated with the Admin role.
     *
     * @return BaseResponse
     */
    @GetMapping("/authenticate")
    public BaseResponse authenticate() {
        return BaseResponse.success("Authenticated", null);
    }

}
