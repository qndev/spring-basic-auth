package io.github.qndev.springbasicauth.controller;

import io.github.qndev.springbasicauth.entity.User;
import io.github.qndev.springbasicauth.repository.UserRepository;
import io.github.qndev.springbasicauth.response.BaseResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicAuthenticationController {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public BasicAuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/generate-user")
    public BaseResponse generateUser(@RequestBody User userRequest) {

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return BaseResponse.success("User generated", user);
    }

    @GetMapping("/authenticate")
    public BaseResponse authenticate() {
        return BaseResponse.success("Authenticated", null);
    }

}
