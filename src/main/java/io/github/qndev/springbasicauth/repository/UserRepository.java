package io.github.qndev.springbasicauth.repository;

import io.github.qndev.springbasicauth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUsername(String username);

}
