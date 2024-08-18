package io.github.qndev.springbasicauth.repository;

import io.github.qndev.springbasicauth.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {
}
