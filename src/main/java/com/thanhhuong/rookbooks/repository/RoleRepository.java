package com.thanhhuong.rookbooks.repository;

import com.thanhhuong.rookbooks.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
