package com.company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.entity.Role;
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByDescription(String description);
}
